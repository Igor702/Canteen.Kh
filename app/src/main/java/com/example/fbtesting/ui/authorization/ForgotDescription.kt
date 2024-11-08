package com.example.fbtesting.ui.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableTextField
import com.example.fbtesting.ui.reusable.ReusableWideButton
import com.example.fbtesting.view_model.authorization.ForgotPassViewModel


@Composable
fun ForgotScreen(
    modifier: Modifier = Modifier, viewModel: ForgotPassViewModel,
    onNavigateToSignIn: () -> Unit,
    onNotifyToastEmptyField: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    val isEmailSent by viewModel.isEmailSent.collectAsStateWithLifecycle()


    if (isEmailSent) {
        onNavigateToSignIn()
    }



    ForgotScreen(email = email,
        onEmailChanged = { email = it },
        onSendForgotPassEmail = {
            if (email.isEmpty()) {
                onNotifyToastEmptyField()
            } else {
                viewModel.sendForgotEmail(email)

            }
        })

}

@Composable
fun ForgotScreen(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (String) -> Unit,
    onSendForgotPassEmail: () -> Unit
) {

    ForgotContentPortrait(email = email,
        onEmailChanged = { onEmailChanged(it) }) { onSendForgotPassEmail() }

}

@Composable
fun ForgotContentPortrait(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (String) -> Unit,
    onSendForgotPassEmail: (String) -> Unit
) {

    Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxSize()) {

        ReusableTextField(
            textValue = email,
            hint = HINT_ENTER_EMAIL,
            inputKeyboardType = KeyboardType.Email,
            onChange = { onEmailChanged(it) })

        ReusableWideButton(
            name = stringResource(R.string.send_email),
            onClick = { onSendForgotPassEmail(email) },
            modifier = modifier.padding(top = dimensionResource(R.dimen.margin_small))
        )

    }

}


@Preview
@Composable
private fun ForgotContentPortraitPreview() {
    MaterialTheme {
        Surface {
            ForgotContentPortrait(email = "",
                onEmailChanged = { }) { }
        }
    }

}


@Preview
@Composable
private fun ForgotScreenPreview() {
    MaterialTheme {
        Surface {
            ForgotScreen(email = "", onEmailChanged = {}) { }
        }
    }

}
