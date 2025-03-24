package com.example.fbtesting

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fbtesting.ui.MenuScreen
import com.example.fbtesting.ui.OrdersSummaryScreen
import com.example.fbtesting.ui.StatusScreen
import com.example.fbtesting.ui.authorization.AuthorizationScreen
import com.example.fbtesting.ui.authorization.ForgotScreen
import com.example.fbtesting.ui.authorization.SignInScreen
import com.example.fbtesting.ui.authorization.SignUpScreen
import com.example.fbtesting.ui.authorization.currentWindowAdaptiveInfo
import com.example.fbtesting.view_model.SharedViewModel
import com.example.fbtesting.view_model.authorization.AuthViewModel
import com.example.fbtesting.view_model.authorization.ForgotPassViewModel
import com.example.fbtesting.view_model.authorization.SignInViewModel
import com.example.fbtesting.view_model.authorization.SignUpViewModel
import kotlinx.serialization.Serializable


@Composable
fun CanteenNavHost(navController: NavHostController, context: Context, onFinish: () -> Unit) {

    NavHost(
        navController = navController,
        startDestination = ScreenAuthorization
    ) {


        composable<ScreenAuthorization> {

            AuthorizationScreen(
                onNavigateToMenu = {
                    navController.graph.setStartDestination<ScreenMenu>()
                    Log.d(TAG, "NavHost, AuthorizationScreen, onNavigateToMenu")
                    navController.navigate(ScreenMenu) {
                        popUpTo<ScreenAuthorization> {
                            inclusive = true
                            saveState = false
                        }

                        restoreState = false
                        launchSingleTop = true

                    }
                },
                onNavigateToSignIn = { navController.navigate(ScreenSignIn) },
                onNavigateToSignUp = { navController.navigate(ScreenSignUp) },
                viewModel = hiltViewModel<AuthViewModel>()
            )
        }

        composable<ScreenSignIn> {
            SignInScreen(
                viewModel = hiltViewModel<SignInViewModel>(),

                onNavigateToMenu = {
                    navController.graph.setStartDestination<ScreenMenu>()
                    Log.d(TAG, "NavHost, SignInScreen, onNavigateToMenu")
                    navController.navigate(ScreenMenu) {
                        popUpTo<ScreenAuthorization> {
                            inclusive = true
                            saveState = false
                        }

                        restoreState = false
                        launchSingleTop = true

                    }


                },

                onForgotPassword = {
                    Log.d(TAG, "NavHost, SignInScreen, onForgotPassword")

                    navController.navigate(
                        ScreenForgot
                    )
                },
                onSignUp = {
                    navController.navigate(ScreenSignUp)
                },

                windowSizeClass = currentWindowAdaptiveInfo(),
                onNotifyToastEmptyFields = {
                    Toast.makeText(
                        context,
                        "Fill up all fields, please!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onNotifyError = { error ->
                    Toast.makeText(
                        context,
                        "Something get wrong, error: $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            )
        }


        composable<ScreenForgot> {

            ForgotScreen(viewModel = hiltViewModel<ForgotPassViewModel>(),
                onNavigateToSignIn = {
                    Log.d(TAG, "NavHost, ForgotScreen, onNavigateToSignIn")
                    navController.popBackStack(ScreenSignIn, inclusive = false, saveState = false)
                },

                onNotifyToastEmptyField = {
                    Toast.makeText(context, "Fill up the field, please", Toast.LENGTH_SHORT)
                        .show()

                },
                onNotifyError = { error ->
                    Toast.makeText(
                        context,
                        "Something get wrong, error: $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                })
        }


        composable<ScreenSignUp> {
            SignUpScreen(windowSizeClass = currentWindowAdaptiveInfo(),
                viewModel = hiltViewModel<SignUpViewModel>(),
                onNotifyToastEmptyFields = {
                    Toast.makeText(context, "Fill up all fields, please!", Toast.LENGTH_SHORT)
                        .show()
                },
                onNavigateToMenu = {
                    navController.graph.setStartDestination<ScreenMenu>()
                    Log.d(TAG, "NavHost, SignUpScreen, onNavigateToMenu")
                    navController.navigate(ScreenMenu) {
                        popUpTo<ScreenAuthorization> {
                            inclusive = true
                            saveState = false
                        }

                        restoreState = false
                        launchSingleTop = true

                    }
                },
                onNotifyError = { error ->
                    Toast.makeText(
                        context,
                        "Something get wrong, error: $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            )
        }


        composable<ScreenMenu> {
            MenuScreen(
                onNavigateToSummary = { navController.navigate(ScreenSummary) },
                viewModel = hiltViewModel<SharedViewModel>(navController.getBackStackEntry<ScreenMenu>()),
                onNotifyLoadingError = { error ->
                    Toast.makeText(
                        context,
                        "Something get wrong, error: $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                },
                onNotifyEmptyOrder = {
                    Toast.makeText(context, "Add some food, please!", Toast.LENGTH_SHORT)
                        .show()
                },
                onFinish = {
                    onFinish()
                }
            )

        }

        composable<ScreenSummary> {

            OrdersSummaryScreen(
                viewModel = hiltViewModel<SharedViewModel>(navController.getBackStackEntry<ScreenMenu>()),
                onNavigateToStatusFragment = { navController.navigate(ScreenStatus) },
                onCancel = { navController.navigate(ScreenMenu) },
                onNotifyNoPaymentMethod = {
                    Toast.makeText(
                        context,
                        context.resources?.getString(R.string.choose_payment_method),
                        Toast.LENGTH_SHORT
                    ).show()
                },

                onNotifyNoFood = {
                    Toast.makeText(
                        context,
                        context.resources?.getString(R.string.add_some_food),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onNotifyMaxDishCount = {
                    Toast.makeText(
                        context,
                        context.resources?.getString(R.string.can_not_increase_more),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onNotifyMinDishCount = {
                    Toast.makeText(
                        context,
                        context.resources?.getString(R.string.can_not_decrease_more),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }


        composable<ScreenStatus> {
            StatusScreen(
                viewModel = hiltViewModel<SharedViewModel>(navController.getBackStackEntry<ScreenMenu>()),
                onExit = {
                    (context as Activity).finish()
                })

        }


    }
}

@Serializable
object ScreenAuthorization

@Serializable
object ScreenSignIn

@Serializable
object ScreenForgot

@Serializable
object ScreenSignUp

@Serializable
object ScreenMenu

@Serializable
object ScreenSummary

@Serializable
object ScreenStatus


//todo: remove
@Serializable
object ScreenPlayground