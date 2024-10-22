package com.example.fbtesting.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.data.TAG
import com.example.fbtesting.databinding.FragmentStatusBinding
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "StatusFragment, onAttach")

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStatusBinding.inflate(inflater, container, false)

        val navEntry = findNavController().getBackStackEntry(R.id.menuFragment)
        val onExit = { activity?.finish() }

        binding.statusComposeView.setContent {
            MaterialTheme {
                Surface {
                    StatusScreen(viewModel = hiltViewModel<SharedViewModel>(navEntry)) {
                        onExit()
                    }
                }
            }
        }

        return binding.root

    }


}