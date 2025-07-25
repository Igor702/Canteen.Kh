package com.example.fbtesting.ui.order

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.fbtesting.MENU_LIST_TAG
import com.example.fbtesting.R
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.ui.reusable.ReusableTitlePriceContent
import com.example.fbtesting.ui.reusable.ReusableWideButton
import com.example.fbtesting.view_model.order.MenuScreenState
import com.example.fbtesting.view_model.order.MenuViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel,
//    restoreState: Boolean,
    onNavigateToSummary: (List<String>) -> Unit,
    onNotifyEmptyOrder: () -> Unit,
    onNotifyLoadingError: (String) -> Unit,
    onFinish: () -> Unit
) {

    val menuScreenState = viewModel.menuData.collectAsStateWithLifecycle()
    var pressedSecondTime by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val list = remember { mutableStateListOf<Dish>() }





    Log.d(TAG, "MenuScreen, menuScreenState: ${menuScreenState.value}")


    BackHandler {
        if (pressedSecondTime) {
            Log.d(com.example.fbtesting.TAG, "MenuScreen button back finish")
            onFinish()
        } else {
            pressedSecondTime = true
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

    LaunchedEffect(menuScreenState.value) {

        when (menuScreenState.value) {
            is MenuScreenState.Loading -> {
                //TODO: make skeleton content loader
            }

            is MenuScreenState.LoadSuccess -> {
                Log.d(TAG, "MenuDescription, LoadSuccess, list is empty")
                if(list.isEmpty()){
                    Log.d(TAG, "MenuDescription, LoadSuccess, list is empty")
                    list.addAll((menuScreenState.value as MenuScreenState.LoadSuccess).list)
                }

            }

            is MenuScreenState.LoadError -> {
                onNotifyLoadingError((menuScreenState.value as MenuScreenState.LoadError).error)
            }

            is MenuScreenState.Navigate -> {
//                //todo: add restore val to MenuScreen
                //restoreStateIfNotEmpty()
                    viewModel.restoreStateIfHas()
                Log.d(TAG, "MenuScreen, Navigate, list.size: ${(menuScreenState.value as MenuScreenState.Navigate).list.size} ")
                onNavigateToSummary((menuScreenState.value as MenuScreenState.Navigate).list)
            }

        }
    }

//    if (menuScreenState.value is MenuScreenState.LoadSuccess){
//        //todo: migrate menuScreen here
//    }

    MenuScreen(
        list = list,
        onSetDishesAndNavigate = {

            if (list.find { it.checked } != null) {
                viewModel.setModifiedList(list)

            } else {
                onNotifyEmptyOrder()
            }
        },
        onCheckChanged = { dish: Dish ->
            list[list.indexOf(dish)] = dish.copy(checked = !dish.checked)

        }
    )


}

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    list: MutableList<Dish>,
    onSetDishesAndNavigate: () -> Unit,
    onCheckChanged: (Dish) -> Unit
) {

    //todo: if list is empty, show skeletons

    Column {
        Column(modifier = Modifier.weight(1f)) {
            MenuListComponent(list = list) { dish: Dish ->
                onCheckChanged(dish)
            }
        }

        Column(modifier = Modifier.weight(0.08f)) {
            ReusableWideButton(name = stringResource(R.string.create_order),
                onClick = {
                    onSetDishesAndNavigate()
                })

        }

    }


}

@Composable
fun MenuListComponent(
    modifier: Modifier = Modifier,
    list: MutableList<Dish>,
    onCheckChanged: (Dish) -> Unit
) {


    LazyColumn(
        modifier = modifier.testTag(MENU_LIST_TAG),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement =
        Arrangement.spacedBy(dimensionResource(R.dimen.margin_small))
    ) {
        items(items = list) { dish ->
            DishCardComponent(
                checked = dish.checked,
                onCheckChanged = {
                    onCheckChanged(dish)
                },
                dishTitle = dish.title,
                price = dish.price,
                url = dish.url
            )

        }
    }
}

@Composable
fun DishCardComponent(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: () -> Unit,
    dishTitle: String,
    price: String,
    url: String
) {

    val color by animateColorAsState(
        targetValue =
        if (checked) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.tertiaryContainer,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),

        label = "colorLabel"
    )


    Surface(
        color = color,
        shape = ShapeDefaults.Medium
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.margin_small))
        ) {
            Surface(
                modifier = modifier
                    .size(105.dp, 105.dp), shape =
                ShapeDefaults.Medium
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

            DishDataComponent(
                modifier = modifier.weight(2.0f), checked = checked,
                onCheckChanged = { onCheckChanged() },
                dishTitle = dishTitle,
                price = price
            )
        }
    }


}


@Composable
fun DishDataComponent(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: () -> Unit,
    dishTitle: String,
    price: String
) {

    Column(
        modifier = modifier
            .padding(start = dimensionResource(R.dimen.margin_extra_small)),
        horizontalAlignment = Alignment.End,
    ) {
        ReusableTitlePriceContent(dishTitle = dishTitle, price = price)

        AddToBasketComponent(checked = checked) { onCheckChanged() }
    }

}

@Composable
fun AddToBasketComponent(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.add_to_basket))
        Checkbox(checked = checked, onCheckedChange = { onCheckChanged(it) })
    }


}

@Preview
@Composable
private fun AddToBasketComponentPreview() {

    MaterialTheme {
        Surface {
            AddToBasketComponent(checked = false) { {} }
        }
    }

}

@Preview
@Composable
private fun DishDataComponentPreview() {
    MaterialTheme {
        Surface {
            var check by remember { mutableStateOf(false) }
            DishDataComponent(
                checked = check,
                onCheckChanged = { check = !check },
                dishTitle = "Title", price = "456"
            )
        }
    }

}

@Preview
@Composable
private fun DishCardComponentPreview() {
    MaterialTheme {
        Surface {
            var check by remember { mutableStateOf(false) }

            DishCardComponent(
                checked = check,
                onCheckChanged = { check = !check },
                dishTitle = "Title", price = "456",
                url = ""
            )
        }
    }

}

@Preview
@Composable
private fun MenuListComponentPreview() {
    val list = mutableListOf(
        Dish(id = "1", title = "title1"),
        Dish(id = "2", title = "title1"),
        Dish(id = "3", title = "title1"),
        Dish(id = "4", title = "title1"),
        Dish(id = "5", title = "title1"),
        Dish(id = "6", title = "title1"),

        )
    MaterialTheme {
        Surface {
            var text by remember { mutableStateOf("init") }
            Column {
                MenuListComponent(list = list) { list ->
                    text = list.toString()
                }
                Text(text = text)
            }

        }
    }

}

@Preview
@Composable
private fun MenuScreenPreview() {
    val list = mutableListOf(
        Dish(id = "1", title = "title1"),
        Dish(id = "2", title = "title1"),
        Dish(id = "3", title = "title1"),
        Dish(id = "4", title = "title1"),
        Dish(id = "5", title = "title1"),
        Dish(id = "6", title = "title1"),

        )
    MaterialTheme {
        Surface {
            MenuScreen(
                list = list,
                onSetDishesAndNavigate = {}
            ) { }
        }
    }

}

