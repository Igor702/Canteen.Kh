package com.example.fbtesting

import android.content.Context
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
import com.example.fbtesting.view_model.authorization.ForgotPassViewModel
import com.example.fbtesting.view_model.authorization.SignInViewModel
import com.example.fbtesting.view_model.authorization.SignUpViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable


@Composable
fun CanteenNavHost(navController: NavHostController, context: Context) {

    NavHost(
        navController = navController,
        startDestination = ScreenAuthorization
    ) {


        composable<ScreenAuthorization> {

            AuthorizationScreen(
                onNavigateToMenu = { navController.navigate(ScreenMenu) },
                onNavigateToSignIn = { navController.navigate(ScreenSignIn) },
                onNavigateToSignUp = { navController.navigate(ScreenSignUp) },
                currentUser = Firebase.auth.currentUser?.email
            )
        }

        composable<ScreenSignIn> {
            SignInScreen(
                viewModel = hiltViewModel<SignInViewModel>(),

                onNavigateToMenu = {
                    navController.navigate(ScreenMenu)
                },

                onForgotPassword = {
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
                    navController.navigate(ScreenSignIn)
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
                    navController.navigate(ScreenMenu)
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
                viewModel = hiltViewModel<SharedViewModel>(navController.getBackStackEntry<ScreenMenu>())
            )

        }

        composable<ScreenSummary> {

            OrdersSummaryScreen(
                viewModel = hiltViewModel<SharedViewModel>(navController.getBackStackEntry<ScreenMenu>()),
                navigateToStatusFragment = { navController.navigate(ScreenStatus) },
                onCancel = { navController.navigate(ScreenMenu) }
            )
        }


        composable<ScreenStatus> {
            StatusScreen(
                viewModel = hiltViewModel<SharedViewModel>(navController.getBackStackEntry<ScreenMenu>()),
                onExit = {

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