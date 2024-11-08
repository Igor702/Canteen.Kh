package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentForgotBinding
import com.example.fbtesting.view_model.authorization.ForgotPassViewModel
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
                ForgotScreen(viewModel = hiltViewModel<ForgotPassViewModel>(),
                    onNavigateToSignIn = {
                        findNavController().navigate(ForgotFragmentDirections.actionForgotFragmentToSignInFragment())
                    },

                    onNotifyToastEmptyField = {
                        Toast.makeText(context, "Fill up the field, please", Toast.LENGTH_SHORT)
                            .show()

                    })
            }


        }

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}