package com.example.fbtesting.data

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.data_models.ui.NavAuthLambdas
import com.example.fbtesting.ui.authorization.AuthorizationFragmentDirections

 object AndroidTestUtils {

    val fakePair = NavAuthLambdas(
        { Log.d(TAG, "navigate to menu")},
        { Log.d(TAG, "navigate to sign in")},
        { Log.d(TAG, "navigate to sign up")}
    )
}