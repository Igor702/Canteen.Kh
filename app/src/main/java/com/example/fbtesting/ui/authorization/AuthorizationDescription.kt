package com.example.fbtesting.ui.authorization

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fbtesting.R
import com.example.fbtesting.data_models.ui.NavAuthLambdas
import com.example.fbtesting.view_model.SharedViewModel


@Composable
fun AuthorizationScreen(modifier: Modifier = Modifier,pair: NavAuthLambdas) {

    AuthorizationContent(modifier = modifier, pair = pair)

}

@Composable
fun AuthorizationContent(modifier: Modifier = Modifier, pair: NavAuthLambdas) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier
        .padding(top = dimensionResource(R.dimen.margin_normal))) {


        AuthorizationButton(name = stringResource(R.string.sign_in), onClick = pair.navigateToSignIn)

        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_normal)),
            shape  = MaterialTheme.shapes.medium,
            onClick = pair.navigateToSignUp) {
            Text(text = stringResource(R.string.sign_up))
        }

    }
}
 

@Composable
fun AuthorizationButton(
    name: String,
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
) {


    Button(onClick = onClick,
        modifier = modifier
            .padding(
                horizontal =
                dimensionResource(R.dimen.margin_normal)
            )
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium){
        Text(text = name)
    }

}



@Preview
@Composable
private fun AuthorizationButtonPreview() {

    MaterialTheme {
        Surface {
            AuthorizationButton(name = stringResource(R.string.sign_in),
                onClick = {},

                )
        }
    }

}