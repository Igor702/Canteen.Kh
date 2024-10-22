package com.example.fbtesting.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentMenuBinding
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        Log.d("TAG", "MenuFragment")


        val navigateToSummary =
            { findNavController().navigate(R.id.action_menuFragment_to_summaryFragment) }

        val navEntry = findNavController().getBackStackEntry(R.id.menuFragment)

        binding.menuComposeView.setContent {
            val viewModel = hiltViewModel<SharedViewModel>(navEntry)
            MenuScreen(onNavigateToSummary = navigateToSummary, viewModel = viewModel)
        }



        return binding.root
    }


}



