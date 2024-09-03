package com.example.fbtesting.ui.authorization

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentAuthorizationBinding
import com.example.fbtesting.getAppComponent
import com.example.fbtesting.view_model.SharedViewModel

class AuthorizationFragment:Fragment() {



    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() =  _binding!!

    private val viewModel: SharedViewModel by viewModels { getAppComponent().viewModelsFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        val auth = viewModel.auth.value

        //if user signed in - navigate to MenuFragment

        if (auth?.currentUser != null) {
            Log.d(TAG, "AuthorizationFragment current user is: ${auth.currentUser}")
            findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMenuFragment())
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

    override fun onPause() {
        Log.d(TAG, "AuthorizationFragment onPause")

        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "AuthorizationFragment onStop")

        super.onStop()
    }




    override fun onDestroy() {
        Log.d(TAG, "AuthorizationFragment onDestroy")
        super.onDestroy()
        _binding = null
    }
}