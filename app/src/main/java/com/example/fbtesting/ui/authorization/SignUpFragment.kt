package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fbtesting.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.apply {
            btnSignUp.setOnClickListener {
               val email =  editTextEmail.text.toString()
               val password =  editTextPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Fill the fields pleas", Toast.LENGTH_SHORT).show()
                }


            }

        }


        return binding.root
    }
}