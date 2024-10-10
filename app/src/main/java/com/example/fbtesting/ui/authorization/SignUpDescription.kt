package com.example.fbtesting.ui.authorization

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableEmailAndPassContent


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    onSignUp: (email: String, password: String) -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    when(windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> {
            Log.d(TAG, "Portrait")

            SignUpContentPortrait(email = email,
                password = password,
                onEmailChanged = {email = it},
                onPasswordChanged = {password = it},
                onSign = {email, password ->  onSignUp(email, password)})

        }
        WindowWidthSizeClass.Medium ->{
            Log.d(TAG, "Landscape, Medium")
            SignUpContentLandscape(email = email,
                password = password,
                onEmailChanged = {email = it},
                onPasswordChanged = {password = it},
                onSign = {email, password ->  onSignUp(email, password)})
        }





    }






}

@Composable
fun SignUpContentPortrait(modifier: Modifier = Modifier,
                          email: String,
                          password: String,
                          onEmailChanged: (String) -> Unit,
                          onPasswordChanged: (String) -> Unit,
                          onSign: (email: String, password: String) -> Unit) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(top = 200.dp))
        ReusableEmailAndPassContent(
            email = email,
            password = password,
            onEmailChanged = {onEmailChanged(it)},
            onPasswordChanged = {onPasswordChanged(it)},
            name = stringResource(R.string.sign_up)) {
                email, password ->  onSign(email, password )
        }
    }
}

@Composable
fun SignUpContentLandscape(modifier: Modifier = Modifier,
                          email: String,
                          password: String,
                          onEmailChanged: (String) -> Unit,
                          onPasswordChanged: (String) -> Unit,
                          onSign: (email: String, password: String) -> Unit) {
    Row(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(top = 50.dp))
        ReusableEmailAndPassContent(
            email = email,
            password = password,
            onEmailChanged = {onEmailChanged(it)},
            onPasswordChanged = {onPasswordChanged(it)},
            name = stringResource(R.string.sign_up)) {
                email, password ->  onSign(email, password )
        }
    }
}



//        WindowWidthSizeClass.Expanded -> {
//            Log.d(TAG, "Landscape, Expanded")
//            MySootheAppLandscape()
//        }



//@Preview
//@Composable
//private fun SignUpScreenPreview() {
//
//    MaterialTheme{
//        Surface {
//            SignUpScreen() { email, password ->  {}}
//        }
//    }
//
//}

@Preview
@Composable
private fun SignUpContentPreview() {

    MaterialTheme {
        Surface {

            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Text(text = email)
            ReusableEmailAndPassContent(
                email = email,
                password = password,
                onEmailChanged = {email = it},
                onPasswordChanged = {password = it},
                name = stringResource(R.string.sign_up)) {
                    email, password ->  {}
            }
        }
    }

}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun currentWindowAdaptiveInfo(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    Log.d(TAG, "currentWindowAdaptiveInfo, config: $configuration, size: $size")
    return WindowSizeClass.calculateFromSize(size)
}