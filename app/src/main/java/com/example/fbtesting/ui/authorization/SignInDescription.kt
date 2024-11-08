package com.example.fbtesting.ui.authorization

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableEmailAndPassContent
import com.example.fbtesting.view_model.authorization.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    viewModel: SignInViewModel,
    onForgotPassword: () -> Unit,
    onSignUp: () -> Unit,
    onNavigateToMenu: () -> Unit,
    onNotifyToastEmptyFields: () -> Unit


) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isSigned by viewModel.isSigned.collectAsStateWithLifecycle()


    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
            onNavigateToMenu()
        },
        onAuthError = {
            user = null
        }
    )

    val token = stringResource(R.string.default_web_client_id)
    val context = LocalContext.current




    if (isSigned) {
        onNavigateToMenu()
    }

    SignInScreen(
        windowSizeClass = windowSizeClass,
        email = email,
        onEmailChanged = { newEmail: String -> email = newEmail },
        password = password,
        onPasswordChanged = { newPass: String -> password = newPass },
        onSignIn = {

            if (email.isEmpty() || password.isEmpty()) {
                onNotifyToastEmptyFields()
            } else {
                viewModel.singInWithEmailAndPassword(email, password)

            }
        },
        onForgotPassword = { onForgotPassword() },


        onSignInWithGoogle = {

            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher.launch(googleSignInClient.signInIntent)

        },
        onSignUp = { onSignUp() }

    )


}


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    onSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onSignUp: () -> Unit

) {


    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Log.d(TAG, "Portrait")

            SignInContentPortrait(
                email = email,
                password = password,
                onEmailChanged = { onEmailChanged(it) },
                onPasswordChanged = { onPasswordChanged(it) },
                onSign = { onSignIn() },
                onForgotPassword = { onForgotPassword() },
                onSignInWithGoogle = onSignInWithGoogle,
                onSignUp = onSignUp


            )

        }

        WindowWidthSizeClass.Medium -> {
            Log.d(TAG, "Landscape, Medium")
            SignInContentLandscape(
                email = email,
                password = password,
                onEmailChanged = { onEmailChanged(it) },
                onPasswordChanged = { onPasswordChanged(it) },
                onSign = { onSignIn() },
                onForgotPassword = { onForgotPassword() },
                onSignInWithGoogle = onSignInWithGoogle,
                onSignUp = onSignUp


            )
        }


    }

}


@Composable
fun SignInContentPortrait(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSign: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 200.dp))
        ReusableEmailAndPassContent(
            email = email,
            password = password,
            onEmailChanged = { onEmailChanged(it) },
            onPasswordChanged = { onPasswordChanged(it) },
            name = stringResource(R.string.sign_in)
        ) {
            onSign()
        }
        Row(
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_normal))
                .clickable { onForgotPassword() }
        ) {

            Text(text = stringResource(R.string.forgot_password))
        }
        Spacer(modifier = Modifier.padding(top = 200.dp))
        SingInWithGoogleContent { onSignInWithGoogle() }
        CreateAccountContent(modifier = modifier.padding(top = dimensionResource(R.dimen.margin_small))) {
            onSignUp()
        }
    }
}

@Composable
fun SignInContentLandscape(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSign: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 50.dp))
        ReusableEmailAndPassContent(
            email = email,
            password = password,
            onEmailChanged = { onEmailChanged(it) },
            onPasswordChanged = { onPasswordChanged(it) },
            name = stringResource(R.string.sign_in)
        ) {
            onSign()
        }

        Row(
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_normal))
                .clickable { onForgotPassword() }
        ) {

            Text(text = stringResource(R.string.forgot_password))
        }
        Spacer(modifier = Modifier.padding(top = 50.dp))
        SingInWithGoogleContent { onSignInWithGoogle() }
        CreateAccountContent(
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_small))
        ) {
            onSignUp()
        }
    }
}


@Composable
fun SingInWithGoogleContent(modifier: Modifier = Modifier, onSignInWithGoogle: () -> Unit) {
    Row(modifier = modifier.clickable { onSignInWithGoogle() }) {
        Image(
            painter = painterResource(R.drawable.sign_in_with_google),
            contentDescription = stringResource(R.string.sign_in_with_google),
            modifier = modifier.size(24.dp, 24.dp)
        )
        Text(
            text = stringResource(R.string.sign_in),
            modifier = modifier.padding(start = dimensionResource(R.dimen.margin_small))
        )

    }
}

@Composable
fun CreateAccountContent(modifier: Modifier = Modifier, onSignUp: () -> Unit) {
    Row(modifier = modifier.clickable { onSignUp() }) {
        Text(text = stringResource(R.string.don_t_have_an_account))
        Text(
            text = stringResource(R.string.sign_up),
            color = colorResource(R.color.purple_200),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.margin_small))
        )
    }
}


@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}


@Preview
@Composable
private fun CreateAccountContentPreview() {
    MaterialTheme {
        Surface {
            CreateAccountContent { }
        }
    }

}

@Preview
@Composable
private fun SignInWithGoogleContentPreview() {
    MaterialTheme {
        Surface {
            SingInWithGoogleContent {}
        }
    }

}

@Preview
@Composable
private fun SignInScreenPortraitPreview() {
    MaterialTheme {
        Surface {
            SignInContentPortrait(email = "",
                password = "",
                onSign = { {} },
                onSignUp = {},
                onEmailChanged = {},
                onSignInWithGoogle = {},
                onForgotPassword = {},
                onPasswordChanged = {})
        }
    }

}

@Preview(widthDp = 640, heightDp = 360)
@Composable
private fun SignInScreenLandscapePreview() {
    MaterialTheme {
        Surface {
            SignInContentLandscape(email = "",
                password = "",
                onSign = { {} },
                onSignUp = {},
                onEmailChanged = {},
                onSignInWithGoogle = {},
                onForgotPassword = {},
                onPasswordChanged = {})
        }
    }

}