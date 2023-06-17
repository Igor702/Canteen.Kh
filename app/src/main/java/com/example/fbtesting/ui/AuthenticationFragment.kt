package com.example.fbtesting.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.view_model.SharedViewModel
import com.example.fbtesting.databinding.FragmentAuthenticationBinding
import com.example.fbtesting.getAppComponent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

private const val TAG = "TAG"

class AuthenticationFragment : Fragment() {


    lateinit var binding: FragmentAuthenticationBinding

    lateinit var launcher: ActivityResultLauncher<Intent>

    private val viewModel: SharedViewModel by viewModels { getAppComponent().viewModelsFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        //take authenticated user from viewModel
        val auth = viewModel.auth.value

        //todo: if user launch app first time, he would navigate to menu without order status

        if (auth?.currentUser != null) {
            Log.d(TAG, "AuthenticationFragment current user is: ${auth.currentUser}")
            findNavController().navigate(R.id.action_authenticationFragment_to_menuFragment)
        }


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

        //listener for signing
        binding.btnSignIn.setOnClickListener {
            signInWithGoogle()
        }

        return binding.root
    }


    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this.requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        val auth = viewModel.auth.value
        auth?.signInWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_authenticationFragment_to_menuFragment)

            } else {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

            }
        }
    }
}