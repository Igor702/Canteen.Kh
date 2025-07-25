package com.example.fbtesting.ui.order

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fbtesting.R
import com.example.fbtesting.data_models.convertOrderToString
import com.example.fbtesting.ui.reusable.ReusableOutlinedButton
import com.example.fbtesting.view_model.order.StatusViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StatusScreen(
    modifier: Modifier = Modifier,
    viewModel: StatusViewModel,
    onExit: () -> Unit,
    serializedOrder: String,
    onNotifySecondPressToExit: ()->Unit
) {


    val cookingStatus = viewModel.cookingStatus.collectAsStateWithLifecycle()
    val currentOrder = viewModel.order.collectAsStateWithLifecycle()
    var pressedSecondTime by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    



    //todo: write test for that
    BackHandler {
        if (pressedSecondTime) {
            Log.d(com.example.fbtesting.TAG, "MenuScreen button back finish")
            onExit()
        } else {
            pressedSecondTime = true
            onNotifySecondPressToExit()
            Log.d(com.example.fbtesting.TAG, "MenuScreen button back pressSecond set to true")

            coroutineScope.launch {
                delay(2000)
                pressedSecondTime = false
                Log.d(
                    com.example.fbtesting.TAG,
                    "MenuScreen button back pressSecond reset to false"
                )

            }
        }
    }

    LaunchedEffect(currentOrder.value) {
        if (currentOrder.value.dishes.isEmpty()) {
            viewModel.setSerializedOrder(serializedOrder)
        }
    }



    StatusScreen(
        status = cookingStatus.value,
        dishesWithCount = currentOrder.value.dishes.convertOrderToString(),
        totalPrice = currentOrder.value.totalPrice,
        payBy = currentOrder.value.payBy
    ) {  onExit()}
}

/*
1 all ui elements are visible
2 changes to orderStatus are visible in ui
3 press exit onExit triggered
 */

@Composable
fun StatusScreen(
    modifier: Modifier = Modifier,
    status: String,
    dishesWithCount: String,
    totalPrice: String,
    payBy: String,
    onExit: () -> Unit
) {


    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .padding(
                    start = dimensionResource(R.dimen.margin_small),
                    top = dimensionResource(R.dimen.margin_big)
                )
        ) {
            Row {
                Text(text = stringResource(R.string.your_order))
                Text(text = dishesWithCount)
            }
            Row {
                Text(text = stringResource(R.string.total_price_is))
                Text(text = totalPrice)
            }
            Row {
                Text(text = stringResource(R.string.pay_by))
                Text(text = payBy)
            }
            Row {
                Text(text = stringResource(R.string.order_status))
                Text(text = status)
            }


        }

        Column(
            modifier = modifier
                .weight(1f)
                .padding(bottom = dimensionResource(R.dimen.margin_small)),
            verticalArrangement = Arrangement.Bottom
        ) {
            ReusableOutlinedButton(text = stringResource(R.string.exit)) {
                onExit()
            }

        }

    }

}


@Preview
@Composable
private fun StatusScreenPreview() {
    MaterialTheme {
        Surface {
            StatusScreen(
                status = "status",
                dishesWithCount = "dishes",
                totalPrice = "123",
                payBy = "card",
                onExit = {}
            )
        }
    }

}

