package com.example.fbtesting.ui.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.R
import com.example.fbtesting.data_models.ui.NavAuthLambdas
import com.example.fbtesting.ui.reusable.ReusableOutlinedButton
import com.example.fbtesting.ui.reusable.ReusableWideButton


@Composable
fun AuthorizationScreen(modifier: Modifier = Modifier, pair: NavAuthLambdas, currentUser: String?) {


    if (!currentUser.isNullOrEmpty()) {
        pair.navigateToMenu().invoke()
    } else {
        AuthorizationContent(modifier = modifier.testTag(AUTHORIZATION_CONTENT_TAG), pair = pair)

    }


}

@Composable
fun AuthorizationContent(modifier: Modifier = Modifier, pair: NavAuthLambdas) {
    Column(
        verticalArrangement = Arrangement.Center, modifier = modifier
            .padding(top = dimensionResource(R.dimen.margin_normal))
    ) {


        ReusableWideButton(
            name = stringResource(R.string.sign_in),
            onClick = pair.navigateToSignIn()
        )


        ReusableOutlinedButton(text = stringResource(R.string.sign_up))
        { pair.navigateToSignUp() }

    }
}
 





