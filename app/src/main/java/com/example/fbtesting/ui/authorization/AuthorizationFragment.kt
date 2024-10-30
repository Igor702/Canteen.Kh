package com.example.fbtesting.ui.authorization

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.databinding.FragmentAuthorizationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {


    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        Log.d(TAG, "AuthorizationFragment onAttach")

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "AuthorizationFragment onCreate")
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)


        fun toMenu(): () -> Unit = {

            findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMenuFragment())
        }

        fun toSignUp(): () -> Unit = {

            findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignUpFragment())
        }

        fun toSignIn(): () -> Unit = {

            findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignInFragment())
        }




        binding.apply {

            authorizationComposeView.setContent {
                MaterialTheme {

                    AuthorizationScreen(
                        onNavigateToMenu = toMenu(),
                        onNavigateToSignIn = toSignIn(),
                        onNavigateToSignUp = toSignUp(),
                        currentUser = Firebase.auth.currentUser?.email
                    )

                }
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