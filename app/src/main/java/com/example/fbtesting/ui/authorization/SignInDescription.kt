package com.example.fbtesting.ui.authorization

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableEmailAndPassContent


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    onSignIn:(email:String, password:String)->Unit,
    onForgotPassword:()->Unit,
    onSignInWithGoogle: () -> Unit,
    onSignUp: () -> Unit

    ) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }



    when(windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> {
            Log.d(TAG, "Portrait")

         SignInContentPortrait(
             email = email,
             password = password,
             onEmailChanged = {email = it},
             onPasswordChanged = {password = it},
             onSign = {email, password ->  onSignIn(email, password)},
             onForgotPassword = {onForgotPassword()},
             onSignInWithGoogle = onSignInWithGoogle,
             onSignUp = onSignUp


         )

        }
        WindowWidthSizeClass.Medium ->{
            Log.d(TAG, "Landscape, Medium")
            SignInContentLandscape(
                email = email,
                password = password,
                onEmailChanged = {email = it},
                onPasswordChanged = {password = it},
                onSign = {email, password ->  onSignIn(email, password)},
                onForgotPassword = {onForgotPassword()},
                onSignInWithGoogle = onSignInWithGoogle,
                onSignUp = onSignUp


            )
        }





    }

    }



@Composable
fun SignInContentPortrait(modifier: Modifier = Modifier,
                          email: String,
                          password: String,
                          onEmailChanged: (String) -> Unit,
                          onPasswordChanged: (String) -> Unit,
                          onSign: (email: String, password: String) -> Unit,
                          onForgotPassword: () -> Unit,
                          onSignInWithGoogle: () -> Unit,
                          onSignUp: () -> Unit) {
    Column(modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(top = 200.dp))
        ReusableEmailAndPassContent(
            email = email,
            password = password,
            onEmailChanged = {onEmailChanged(it)},
            onPasswordChanged = {onPasswordChanged(it)},
            name = stringResource(R.string.sign_in)) {
                email, password ->  onSign(email, password )
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
fun SignInContentLandscape(modifier: Modifier = Modifier,
                          email: String,
                          password: String,
                          onEmailChanged: (String) -> Unit,
                          onPasswordChanged: (String) -> Unit,
                          onSign: (email: String, password: String) -> Unit,
                          onForgotPassword: () -> Unit,
                          onSignInWithGoogle: () -> Unit,
                          onSignUp: () -> Unit) {
    Column (modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(top = 50.dp))
        ReusableEmailAndPassContent(
            email = email,
            password = password,
            onEmailChanged = {onEmailChanged(it)},
            onPasswordChanged = {onPasswordChanged(it)},
            name = stringResource(R.string.sign_in)) {
                email, password ->  onSign(email, password )
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
        CreateAccountContent(modifier = modifier
            .padding(top = dimensionResource(R.dimen.margin_small))) {
            onSignUp()
        }
    }
}


@Composable
fun SingInWithGoogleContent(modifier: Modifier = Modifier, onSignInWithGoogle:()->Unit) {
    Row(modifier = modifier.clickable { onSignInWithGoogle() }) {
        Image(painter = painterResource(R.drawable.sign_in_with_google),
            contentDescription = stringResource(R.string.sign_in_with_google),
            modifier = modifier.size(24.dp, 24.dp))
        Text(text = stringResource(R.string.sign_in),
            modifier = modifier.padding(start = dimensionResource(R.dimen.margin_small)))

    }
}

@Composable
fun CreateAccountContent(modifier: Modifier = Modifier, onSignUp:()->Unit) {
    Row(modifier = modifier.clickable { onSignUp() }) {
        Text(text = stringResource(R.string.don_t_have_an_account))
        Text(text = stringResource(R.string.sign_up),
            color = colorResource(R.color.purple_200),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.margin_small)))
    }
}

@Preview
@Composable
private fun CreateAccountContentPreview() {
    MaterialTheme{
        Surface {
            CreateAccountContent {  }
        }
    }
    
}

@Preview
@Composable
private fun SignInWithGoogleContentPreview() {
    MaterialTheme{
        Surface {
            SingInWithGoogleContent(){}
        }
    }

}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
private fun SignInScreenPortraitPreview() {
    MaterialTheme{
        Surface {
            SignInContentPortrait(email = "",
                password = "",
                onSign = {email, password -> {} },
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
    MaterialTheme{
        Surface {
            SignInContentLandscape(email = "",
                password = "",
                onSign = {email, password -> {} },
                onSignUp = {},
                onEmailChanged = {},
                onSignInWithGoogle = {},
                onForgotPassword = {},
                onPasswordChanged = {})
        }
    }

}