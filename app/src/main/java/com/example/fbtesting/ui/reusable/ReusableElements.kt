package com.example.fbtesting.ui.reusable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.HINT_ENTER_PASS
import com.example.fbtesting.R

@Composable
fun ReusableWideButton(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {


    Button(
        onClick = onClick,
        modifier = modifier
            .padding(
                horizontal =
                dimensionResource(R.dimen.margin_normal)
            )
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = name)
    }

}

@Composable
fun ReusableTextField(
    textValue: String,
    hint: String,
    onChange: (String) -> Unit,
    inputKeyboardType: KeyboardType,
    modifier: Modifier = Modifier
) {


    TextField(
        value = textValue,
        onValueChange = { onChange(it) },
        label = { Text(hint) },
        keyboardOptions = KeyboardOptions(keyboardType = inputKeyboardType),
        modifier = modifier
            .padding(
                horizontal =
                dimensionResource(R.dimen.margin_normal)
            )
            .fillMaxWidth()
    )

}


@Composable
fun ReusableEmailAndPassContent(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSign: (email: String, password: String) -> Unit
) {


    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .padding(top = dimensionResource(R.dimen.margin_normal))
    ) {

        ReusableTextField(
            textValue = email,
            hint = HINT_ENTER_EMAIL,
            onChange = { onEmailChanged(it) },
            inputKeyboardType = KeyboardType.Email,
            modifier = modifier
        )

        ReusableTextField(
            textValue = password,
            hint = HINT_ENTER_PASS,
            onChange = { onPasswordChanged(it) },
            inputKeyboardType = KeyboardType.Password,
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_small))
        )

        ReusableWideButton(
            name = name,
            onClick = { onSign(email, password) },
            modifier = modifier
                .padding(top = dimensionResource(R.dimen.margin_small))
        )


    }

}


@Preview
@Composable
private fun ReusableWideButtonPreview() {

    MaterialTheme {
        Surface {
            ReusableWideButton(
                name = stringResource(R.string.sign_in),
                onClick = {},

                )
        }
    }

}


@Preview
@Composable
private fun ReusableTextFieldPreview() {
    MaterialTheme {
        Surface {
            var initText by remember { mutableStateOf("") }
            val bottomPaddingModifier = Modifier.padding(bottom = 16.dp)

            Column {
                Text(modifier = Modifier, text = initText)
                ReusableTextField(
                    textValue = initText,
                    hint = HINT_ENTER_EMAIL,
                    modifier = bottomPaddingModifier,
                    inputKeyboardType = KeyboardType.Email,
                    onChange = { initText = it }
                )

            }
        }

    }
}