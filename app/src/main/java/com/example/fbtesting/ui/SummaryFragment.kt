package com.example.fbtesting.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.data.TAG
import com.example.fbtesting.databinding.FragmentSummaryBinding
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "SummaryFragment onAttach")

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSummaryBinding.inflate(inflater, container, false)

        val navEntry = findNavController().getBackStackEntry(R.id.menuFragment)
        val navigateToStatusFragment =
            { findNavController().navigate(R.id.action_summaryFragment_to_statusFragment) }
        val onCancel = { findNavController().navigate(R.id.action_summaryFragment_to_menuFragment) }
        binding.summaryComposeView.setContent {
            val viewModel = hiltViewModel<SharedViewModel>(navEntry)

            MaterialTheme {
                Surface {
                    OrdersSummaryScreen(
                        viewModel = viewModel,
                        navigateToStatusFragment = navigateToStatusFragment,
                        onCancel = onCancel
                    )

                }
            }
        }

        return binding.root


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

    }
}









