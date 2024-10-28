package com.example.fbtesting.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fbtesting.PAY_BY_CARD
import com.example.fbtesting.PAY_BY_CASH
import com.example.fbtesting.R
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.ui.reusable.ReusableCardContent
import com.example.fbtesting.ui.reusable.ReusableDoCancelButtons
import com.example.fbtesting.ui.reusable.ReusableTitlePriceContent
import com.example.fbtesting.view_model.SharedViewModel


@Composable
fun OrdersSummaryScreen(
    modifier: Modifier = Modifier,
    context: Context?,
    viewModel: SharedViewModel,
    navigateToStatusFragment: () -> Unit,
    onCancel: () -> Unit
) {

    val list = remember { viewModel.getChosenDishes() }
    var totalPrice by rememberSaveable { mutableIntStateOf(viewModel.getInitPrice()) }
    var selectedOption by rememberSaveable { mutableStateOf("") }


    Column {
        Column(modifier = Modifier.weight(1f)) {
            OrdersListContent(list = list,
                onPlus = { dish: Dish, count: Int ->
                    totalPrice += dish.price.toInt()
                    viewModel.setDishesCount(dish.title, count)
                },
                onMinus = { dish: Dish, count: Int ->
                    totalPrice -= dish.price.toInt()
                    viewModel.setDishesCount(dish.title, count)

                })
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top =
                    dimensionResource(R.dimen.margin_normal)
                )
        ) {


            Column(modifier = modifier.weight(1f)) {
                OrdersPriceAndPaymentMethodContent(
                    totalPrice = totalPrice,
                    selectedOption = selectedOption
                ) {

                    selectedOption = it
                }
            }


            Column(
                modifier = modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                OrdersCreateAndCancelButtonsContent(
                    totalPrice = totalPrice,
                    selectedOption = selectedOption,
                    onSendOrder = {
                        viewModel.sendOrder(
                            totalPrice = totalPrice.toString(),
                            payBy = selectedOption
                        )
                        navigateToStatusFragment()
                    },
                    onMakeToastNoFood = {
                        Toast.makeText(
                            context,
                            context?.resources?.getString(R.string.add_some_food),
                            Toast.LENGTH_SHORT
                        ).show()
                    },

                    onMakeToastNoPaymentMethod = {
                        Toast.makeText(
                            context,
                            context?.resources?.getString(R.string.choose_payment_method),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCancel = {
                        onCancel()
                    }
                )
            }
        }


    }

}

@Composable
fun OrdersCreateAndCancelButtonsContent(
    modifier: Modifier = Modifier,
    totalPrice: Int,
    selectedOption: String,
    onSendOrder: () -> Unit,
    onMakeToastNoFood: () -> Unit,
    onMakeToastNoPaymentMethod: () -> Unit,
    onCancel: () -> Unit,
) {
    ReusableDoCancelButtons(doName = stringResource(R.string.create_order),
        cancelName = stringResource(R.string.cancel),
        onDoClick = {
            if (totalPrice == 0) {

                onMakeToastNoFood()

            } else if (selectedOption == "") {

                onMakeToastNoPaymentMethod()
            } else {

                onSendOrder()
            }
        }, onCancelClick = {
            onCancel()
        }
    )
}


@Composable
fun OrdersPriceAndPaymentMethodContent(
    modifier: Modifier = Modifier,
    totalPrice: Int,
    selectedOption: String,
    onClick: (String) -> Unit
) {

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.margin_small))
        ) {


            Text(text = stringResource(R.string.total_price_is))
            Text(
                text = totalPrice.toString(), modifier = Modifier.padding(
                    start =
                    dimensionResource(R.dimen.margin_extra_small)
                )
            )
        }

        Row(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.margin_small))
        ) {

            Text(text = stringResource(R.string.pay_by))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == PAY_BY_CARD,
                onClick = { onClick(PAY_BY_CARD) }
            )

            Text(text = stringResource(R.string.card))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioButton(
                selected = selectedOption == PAY_BY_CASH,
                onClick = { onClick(PAY_BY_CASH) }
            )
            Text(text = stringResource(R.string.cash))

        }

    }


}


@Composable
fun OrdersListContent(
    modifier: Modifier = Modifier,
    list: MutableList<Dish>,
    onPlus: (Dish, Int) -> Unit, onMinus: (Dish, Int) -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement =
        Arrangement.spacedBy(dimensionResource(R.dimen.margin_small))
    ) {

        items(items = list) { dish: Dish ->
            //todo: relocate this state countOfDish to VM,
            // because we have mapWithCount
            var countOfDish by rememberSaveable { mutableIntStateOf(1) }
            OrderCardContent(
                url = dish.url,
                price = dish.price,
                title = dish.title,
                onPlus = {
                    if (countOfDish <= 12) {
                        countOfDish++
                        onPlus(dish, countOfDish)
                    }
                },
                onMinus = {
                    if (countOfDish != 0) {
                        countOfDish--
                        onMinus(dish, countOfDish)
                    }
                },
                count = countOfDish
            )

        }

    }


}

@Composable
fun OrderCardContent(
    modifier: Modifier = Modifier,
    url: String,
    price: String,
    title: String,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    count: Int
) {
    ReusableCardContent(imageUrl = url) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxHeight()) {
            Column(modifier = Modifier.weight(1.0f), horizontalAlignment = Alignment.End) {
                DishCountContent(count = count, onPlus = onPlus, onMinus = onMinus)

            }
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxHeight(), horizontalAlignment = Alignment.End
            ) {
                ReusableTitlePriceContent(dishTitle = title, price = price)

            }


        }

    }


}


@Composable
fun DishCountContent(
    modifier: Modifier = Modifier,
    count: Int,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(painter = painterResource(R.drawable.baseline_decrease_24),
            contentDescription = stringResource(R.string.increase_count),
            Modifier.clickable {
                onMinus()
            }
        )
        Text(text = count.toString())

        Icon(painter = painterResource(R.drawable.baseline_increase_24),
            contentDescription = stringResource(R.string.decrease_count),
            Modifier.clickable { onPlus() })

    }


}


@Preview
@Composable
private fun OrdersCreateAndCancelButtonsContentPreview() {

    MaterialTheme {
        Surface {
            OrdersCreateAndCancelButtonsContent(totalPrice = 200,
                selectedOption = "",
                onSendOrder = {},
                onMakeToastNoFood = {},
                onMakeToastNoPaymentMethod = {},
                onCancel = {})
        }
    }

}

@Preview
@Composable
private fun OrdersPriceAndPaymentMethodContentPreview() {
    MaterialTheme {
        Surface {
            OrdersPriceAndPaymentMethodContent(totalPrice = 200, selectedOption = "") { }
        }
    }

}

@Preview
@Composable
private fun OrderListContentPreview() {
    val list = mutableListOf(
        Dish(id = "1", title = "title1", price = "60"),
        Dish(id = "2", title = "title1", price = "60"),
        Dish(id = "3", title = "title1", price = "60"),
        Dish(id = "4", title = "title1", price = "60"),
        Dish(id = "5", title = "title1", price = "60"),
        Dish(id = "6", title = "title1", price = "60"),
        Dish(id = "7", title = "title1", price = "60"),

        )
    MaterialTheme {
        Surface {
            OrdersListContent(
                list = list,
                onPlus = { dish: Dish, i: Int -> }) { dish: Dish, i: Int -> }
        }
    }

}


@Preview
@Composable
private fun OderCardContentPreview() {
    MaterialTheme {
        Surface {
            OrderCardContent(url = "url", price = "price1", title = "title1",
                onPlus = {}, onMinus = {}, count = 0
            )
        }
    }
}

@Preview
@Composable
private fun DishCountContentPreview() {
    MaterialTheme {

        var count by remember { mutableIntStateOf(0) }

        Surface {
            DishCountContent(count = count, onPlus = { count++ }) { count-- }
        }
    }

}