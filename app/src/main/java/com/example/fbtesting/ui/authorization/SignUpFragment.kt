package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentSignUpBinding
import com.example.fbtesting.view_model.authorization.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.apply {

            signUpComposeView.setContent {
                SignUpScreen(windowSizeClass = currentWindowAdaptiveInfo(),
                    viewModel = hiltViewModel<SignUpViewModel>(),
                    onNotifyToastEmptyFields = {
                        Toast.makeText(context, "Fill up all fields, please!", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onNavigateToMenu = {
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToMenuFragment())
                    }
                )
            }
        }
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}