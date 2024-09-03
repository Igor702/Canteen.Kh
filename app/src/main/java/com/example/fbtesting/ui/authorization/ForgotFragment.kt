package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentForgotBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotFragment:Fragment() {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)

        binding.apply {
            btnSendEmail.setOnClickListener {
                val email =editTextEmail.text.toString()

                if (email.isEmpty()){
                    Toast.makeText(context, "Enter your email please", Toast.LENGTH_SHORT).show()
                }else{
                    Firebase.auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(ForgotFragmentDirections.actionForgotFragmentToSignInFragment())

                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Email sending error", Toast.LENGTH_SHORT).show()

                        }

                }
            }
        }



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}