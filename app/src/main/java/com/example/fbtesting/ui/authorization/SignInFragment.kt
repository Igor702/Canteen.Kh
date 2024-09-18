package com.example.fbtesting.ui.authorization

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
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentSignInBinding
import com.example.fbtesting.view_model.SharedViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "TAG"

@AndroidEntryPoint
class SignInFragment : Fragment() {


    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        auth = Firebase.auth


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

        binding.apply {

            //listener for sign in with email
            btnSignIn.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Fill the fields pleas", Toast.LENGTH_SHORT).show()
                } else {
                    signInWithEmail(email, password)
                }


            }

            //listener for sign in with Google
            linearLayoutSignInGoogle.setOnClickListener {
                signInWithGoogle()
            }
            //listener for forgot pass button
            btnForgotPassword.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgotFragment())
            }

            btnSignUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }

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
                findNavController().navigate(R.id.action_signInFragment_to_menuFragment)

            } else {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(
                        context,
                        "Authentication success.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMenuFragment())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }


    override fun onDestroy() {
        Log.d(TAG, "SignInFragment onDestroy")

        super.onDestroy()
        _binding = null
    }
}