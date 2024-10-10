package com.example.fbtesting.ui.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.HINT_ENTER_PASS
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableTextField
import com.example.fbtesting.ui.reusable.ReusableWideButton


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUp: (email: String, password: String) -> Unit
) {

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        SignUpContent { email, password ->  onSignUp(email, password ) }

    }

}


@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    onSignUp: (email: String, password: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .padding(top = dimensionResource(R.dimen.margin_normal))
    ) {

        ReusableTextField(
            textValue = email,
            hint = HINT_ENTER_EMAIL,
            onChange = { email = it },
            modifier = modifier
        )

        ReusableTextField(
            textValue = password,
            hint = HINT_ENTER_PASS,
            onChange = { password = it },
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_small))
        )

        ReusableWideButton(
            name = stringResource(R.string.sign_up),
            onClick = { onSignUp(email, password) },
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_small))
        )


    }

}


@Preview
@Composable
private fun SignUpScreenPreview() {

    MaterialTheme{
        Surface {
            SignUpScreen { email, password ->  {}}
        }
    }
    
}

@Preview
@Composable
private fun SignUpContentPreview() {

    MaterialTheme {
        Surface {
            var text by remember { mutableStateOf("init") }
            Text(text = text)
            SignUpContent { email, password ->
                text = "new text: $email, $password"
            }
        }
    }

}