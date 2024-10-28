package com.example.fbtesting.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.fbtesting.R
import com.example.fbtesting.ui.reusable.ReusableOutlinedButton
import com.example.fbtesting.view_model.SharedViewModel

@Composable
fun StatusScreen(modifier: Modifier = Modifier, viewModel: SharedViewModel, onExit: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        val status = viewModel.orderStatus.collectAsState()
        Column(
            modifier = modifier
                .padding(
                    start = dimensionResource(R.dimen.margin_small),
                    top = dimensionResource(R.dimen.margin_big)
                )
        ) {
            Row {
                Text(text = stringResource(R.string.your_order))
                Text(text = viewModel.getStatusOrderDishesWithCountString())
            }
            Row {
                Text(text = stringResource(R.string.total_price_is))
                Text(text = viewModel.getStatusOrderTotalPrice())
            }
            Row {
                Text(text = stringResource(R.string.pay_by))
                Text(text = viewModel.getStatusPayBy())
            }
            Row {
                Text(text = stringResource(R.string.order_status))
                Text(text = status.value)
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

