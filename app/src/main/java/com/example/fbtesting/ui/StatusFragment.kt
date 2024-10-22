package com.example.fbtesting.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fbtesting.MainActivity
import com.example.fbtesting.R
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.convertOrderToString
import com.example.fbtesting.data_models.toOrder
import com.example.fbtesting.databinding.FragmentStatusBinding
import com.example.fbtesting.view_model.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

        val navEntry =   findNavController().getBackStackEntry(R.id.menuFragment)
        val onExit = {activity?.finish()}

        binding.statusComposeView.setContent {
            MaterialTheme {
                Surface {
                    StatusScreen(viewModel = hiltViewModel<SharedViewModel>(navEntry)){
                        onExit()
                    }
                }
            }
        }

                return binding.root

    }


}