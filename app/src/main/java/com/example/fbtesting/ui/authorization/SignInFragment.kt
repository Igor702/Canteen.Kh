package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentSignInBinding
import com.example.fbtesting.view_model.authorization.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "TAG"

@AndroidEntryPoint
class SignInFragment : Fragment() {


    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)



        binding.apply {
            singInComposeView.setContent {
                MaterialTheme {
                    Surface {

                        SignInScreen(
                            viewModel = hiltViewModel<SignInViewModel>(),

                            onNavigateToMenu = {
                                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMenuFragment())
                            },

                            onForgotPassword = {
                                findNavController().navigate(
                                    SignInFragmentDirections.actionSignInFragmentToForgotFragment()
                                )
                            },
                            onSignUp = {
                                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
                            },

                            windowSizeClass = currentWindowAdaptiveInfo(),
                            onNotifyToastEmptyFields = {
                                Toast.makeText(
                                    context,
                                    "Fill up all fields, please!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }

        return binding.root
    }


    override fun onDestroy() {
        Log.d(TAG, "SignInFragment onDestroy")

        super.onDestroy()
        _binding = null
    }
}