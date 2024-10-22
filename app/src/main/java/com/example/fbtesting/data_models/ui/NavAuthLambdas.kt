package com.example.fbtesting.data_models.ui

import android.util.Log
import com.example.fbtesting.data.TAG

open class NavAuthLambdas(
    private var navigateToMenu: () -> Unit,
    private val navigateToSignIn: () -> Unit,
    private val navigateToSignUp: () -> Unit,
) {
    fun navigateToMenu(): () -> Unit {
        Log.d(TAG, "navigateToMenu, navigate: ${navigateToMenu}")
        return navigateToMenu
    }

    open fun navigateToSignIn(): () -> Unit {
        return navigateToSignIn
    }

    open fun navigateToSignUp(): () -> Unit {
        return navigateToSignUp
    }

    fun setNavigateToMenu(navigation: () -> Unit) {
        navigateToMenu = navigation

    }
}
