package com.example.fbtesting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentMenuBinding
import com.example.fbtesting.getAppComponent
import com.example.fbtesting.model.TAG
import com.example.fbtesting.ui.adapters.MenuDatabaseAdapter
import com.example.fbtesting.view_model.SharedViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class MenuFragment : Fragment() {
    private var isBackButtonWasPressed = false

    lateinit var launcher: ActivityResultLauncher<Intent>
    private val args: MenuFragmentArgs by navArgs()




    private lateinit var adapter: MenuDatabaseAdapter
    private val viewModel: SharedViewModel by viewModels { getAppComponent().viewModelsFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        Log.d("TAG", "MenuFragment")


        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("TAG", "apiException: $e")
            }
        }


            viewModel.options.observe(this.viewLifecycleOwner) {
                adapter = MenuDatabaseAdapter(userToBooleanValue(viewModel))
                Log.d(TAG, "MenuFragment, before submit list, temp: $it")


                adapter.submitList(it)

                binding.recyclerViewMenu.adapter = adapter

            }

        binding.btnToOrder.setOnClickListener {
            Log.d(TAG, "btnToOrder, ${MenuDatabaseAdapter.dishes}")
            if (userToBooleanValue(viewModel)){
                if (MenuDatabaseAdapter.dishes.size != 0) {
                    isBackButtonWasPressed = false
                    findNavController().navigate(R.id.action_menuFragment_to_summaryFragment)
                } else {
                    Toast.makeText(context, "Choose some dish!!!", Toast.LENGTH_SHORT).show()

                }
            }else{
                Toast.makeText(context, "Sign in for ordering food!!!", Toast.LENGTH_LONG).show()
            }


        }


        return binding.root
    }

    private fun getClient(): GoogleSignInClient {
        Log.d(TAG, "MenuFragment, getClient()")
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this.requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        Log.d(TAG, "MenuFragment, signInWithGoogle()")

        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }
    private fun firebaseAuthWithGoogle(token: String) {

        val credential = GoogleAuthProvider.getCredential(token, null)
        val auth = viewModel.auth.value
        auth?.signInWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.action_authenticationFragment_to_menuFragment)

                adapter.sortValue = true

            } else {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                if (isBackButtonWasPressed){
                    requireActivity().finish()
                }else{
                    Toast.makeText(context, "Press back button again for exit", Toast.LENGTH_SHORT).show()
                    isBackButtonWasPressed = true

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sign_menu, menu)
        val signIn: MenuItem = menu.findItem(R.id.sign_in)
        val signOut: MenuItem = menu.findItem(R.id.sing_out)

        if (viewModel.auth.value?.currentUser != null){
            signIn.isVisible = false
            signOut.isVisible = true
        }else{
            signIn.isVisible = true
            signOut.isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.sign_in ->{
                signInWithGoogle()
                true
            }

         else ->  return super.onOptionsItemSelected(item)
        }

    }




}

private fun userToBooleanValue(viewModel: SharedViewModel): Boolean{
    if (viewModel.auth.value?.currentUser != null){
        return true
    }
    return false
}

