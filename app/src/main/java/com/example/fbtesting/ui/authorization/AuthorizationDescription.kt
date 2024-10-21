package com.example.fbtesting.ui.authorization

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.R
import com.example.fbtesting.data_models.ui.NavAuthLambdas
import com.example.fbtesting.ui.reusable.ReusableOutlinedButton
import com.example.fbtesting.ui.reusable.ReusableWideButton
import com.example.fbtesting.view_model.SharedViewModel


@Composable
fun AuthorizationScreen(modifier: Modifier = Modifier, pair: NavAuthLambdas, currentUser:String?) {
//    val viewModel: SharedViewModel = viewModel()
//    val currentUser = viewModel.currentUserEmail
    Log.d(TAG, "AuthorizationScreen")

    if (!currentUser.isNullOrEmpty()) {
        Log.d(TAG, "AuthorizationScreen navigate to menu")
        pair.navigateToMenu().invoke()
    } else {
        Log.d(TAG, "AuthorizationScreen draw screen")
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
        {pair.navigateToSignUp() }

    }
}
 





