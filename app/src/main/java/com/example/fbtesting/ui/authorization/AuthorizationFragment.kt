package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.data_models.ui.NavAuthLambdas
import com.example.fbtesting.databinding.FragmentAuthorizationBinding
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {


    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)



        //if user signed in - navigate to MenuFragment
        val pair = NavAuthLambdas(
            {findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMenuFragment())},
            {findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignInFragment()) },
            {findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignUpFragment())
            }
        )




        binding.apply {
            authorizationComposeView.setContent {
                MaterialTheme {
                    val viewModel:SharedViewModel = viewModel()
                    val currentUser by viewModel.currentUserEmail.observeAsState()

                    if (!currentUser.isNullOrEmpty()){
                        pair.navigateToMenu.invoke()

                    }else{
                        AuthorizationScreen(pair = pair)

                    }

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