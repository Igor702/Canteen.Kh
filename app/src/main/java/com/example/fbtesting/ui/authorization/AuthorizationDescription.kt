package com.example.fbtesting.ui.authorization

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.R
import com.example.fbtesting.TAG
import com.example.fbtesting.ui.reusable.ReusableOutlinedButton
import com.example.fbtesting.ui.reusable.ReusableWideButton
import com.example.fbtesting.view_model.authorization.AuthViewModel

@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: AuthViewModel
) {

    AuthorizationScreen(
        onNavigateToMenu = { onNavigateToMenu() },
        onNavigateToSignIn = { onNavigateToSignIn() },
        onNavigateToSignUp = { onNavigateToSignUp() },
        currentUser = viewModel.currentUserEmail.collectAsState().value

    )
}


@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    currentUser: String?
) {


    if (!currentUser.isNullOrEmpty()) {
        Log.d(TAG, "Authorization, user:$currentUser")
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
            .fillMaxHeight()
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
 





