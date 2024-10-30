package com.example.fbtesting.ui.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableOutlinedButton
import com.example.fbtesting.ui.reusable.ReusableWideButton


@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    currentUser: String?
) {


    if (!currentUser.isNullOrEmpty()) {
        onNavigateToMenu()
    } else {
        AuthorizationContent(
            modifier = modifier.testTag(AUTHORIZATION_CONTENT_TAG),
            onNavigateToSignIn = onNavigateToSignIn,
            onNavigateToSignUp = onNavigateToSignUp
        )

    }


}

@Composable
fun AuthorizationContent(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center, modifier = modifier
            .padding(top = dimensionResource(R.dimen.margin_normal))
    ) {


        ReusableWideButton(
            name = stringResource(R.string.sign_in),
            onClick = { onNavigateToSignIn() }
        )


        ReusableOutlinedButton(text = stringResource(R.string.sign_up))
        { onNavigateToSignUp() }

    }
}


@Preview
@Composable
private fun AuthorizationScreenPreview() {
    MaterialTheme {
        Surface {
            AuthorizationScreen(
                onNavigateToMenu = {},
                onNavigateToSignIn = {},
                onNavigateToSignUp = {},
                currentUser = ""
            )
        }
    }

}
 





