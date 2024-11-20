package com.example.fbtesting

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                CanteenApp()

            }

        }
    }
}


@Composable
fun CanteenApp() {

    //TODO: change back stack behaviour
    MaterialTheme {
        val navController = rememberNavController()
        val context = LocalContext.current

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
                        //Todo: make this toast work
                        Toast.makeText(
                            context,
                            "Fill up all fields, please!",
                            Toast.LENGTH_SHORT
                        ).show()
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





