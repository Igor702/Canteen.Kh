package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment: Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.apply {


            btnSignUp.setOnClickListener {

               val email =  editTextEmail.text.toString()
               val password =  editTextPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Fill the fields pleas", Toast.LENGTH_SHORT).show()
                }else{
                 createUserWithEmail(email, password)
                }


            }

        }


        return binding.root
    }

    private fun createUserWithEmail(email: String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        context,
                        "Authentication success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToMenuFragment())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}