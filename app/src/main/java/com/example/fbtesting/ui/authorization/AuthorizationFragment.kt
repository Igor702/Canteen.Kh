package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentAuthorizationBinding
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {


    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)


        viewModel.currentUserEmail.observe(this.viewLifecycleOwner){

            Log.d(TAG, "AuthorizationFragment current user is: ${viewModel.currentUserEmail.value}")
            if (!viewModel.currentUserEmail.value.isNullOrEmpty()) {
                Log.d(TAG, "AuthorizationFragment, not null current user is: ${viewModel.currentUserEmail.value}")
                this.findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMenuFragment())
            }
        }

        //if user signed in - navigate to MenuFragment




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