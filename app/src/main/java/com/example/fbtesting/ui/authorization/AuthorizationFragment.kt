package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.MyApplication
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentAuthorizationBinding
import com.example.fbtesting.view_model.SharedViewModel

class AuthorizationFragment:Fragment() {



    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() =  _binding!!

    private val viewModel: SharedViewModel by viewModels {
        (this.requireActivity().application as MyApplication)
            .daggerComponent.viewModelsFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)


        //if user signed in - navigate to MenuFragment
        Log.d(TAG, "AuthorizationFragment current user is: ${viewModel.userEmail.value}")

        if (viewModel.userEmail.value != null) {
            println("email: ${viewModel.userEmail.value}")
            Log.d(TAG, "AuthorizationFragment current user is: ${viewModel.userEmail.value}")
            findNavController().navigate(R.id.action_authorizationFragment_to_menuFragment)
        }

        binding.apply {

            //user can sign in or sing up here
            btnSignInAuth.setOnClickListener {
                findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignInFragment())
            }
            btnSignUpAuth.setOnClickListener {
                findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignUpFragment())
            }
        }

        return binding.root
    }




    override fun onDestroy() {
        Log.d(TAG, "AuthorizationFragment onDestroy")
        super.onDestroy()
        _binding = null
    }
}