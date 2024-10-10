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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotFragment : Fragment() {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)

        binding.apply {
            forgotComposeView.setContent {
                ForgotScreen { email ->
                    sendForgotEmail(email)
                }
            }


        }



        return binding.root
    }

    private fun sendForgotEmail(email: String) {
        if (email.isEmpty()) {
            Toast.makeText(context, "Enter your email please", Toast.LENGTH_SHORT).show()
        } else {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(ForgotFragmentDirections.actionForgotFragmentToSignInFragment())

                }
                .addOnFailureListener {
                    Toast.makeText(context, "Email sending error", Toast.LENGTH_SHORT)
                        .show()

                }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}