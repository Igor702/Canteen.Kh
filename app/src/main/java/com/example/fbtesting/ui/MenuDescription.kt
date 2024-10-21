package com.example.fbtesting.ui

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fbtesting.R
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.ui.authorization.TAG
import com.example.fbtesting.ui.reusable.ReusableTitlePriceContent
import com.example.fbtesting.ui.reusable.ReusableWideButton
import com.example.fbtesting.view_model.SharedViewModel

private fun getDishes() = List(30) { i -> Dish(i.toString(), "Task # $i") }

@Composable
fun MenuScreen(modifier: Modifier = Modifier, viewModel: SharedViewModel, onNavigateToSummary:()->Unit) {
    Log.d(TAG, "MenuScreen, viewModel hash:${viewModel.hashCode()}")

    val list = remember { viewModel.menuData }
    Log.d(TAG, "MenuScreen, list:${list.toList()}")


    Column {
        Column(modifier = Modifier.weight(1f)) {
            MenuListComponent(list = list) {dish: Dish ->
                list[list.indexOf(dish)] = dish.copy(checked = !dish.checked)
            }
        }

        Column(modifier = Modifier.weight(0.08f)) {
            ReusableWideButton(name = stringResource(R.string.create_order),
                onClick = {
                    Log.d(TAG, "MenuScreen, setDishes onClick, list: ${list.toList()}")
                    if(viewModel.setDishes(list.toList())){
                        onNavigateToSummary()
                    }
                })

        }

    }


    
}

@Composable
fun MenuListComponent(modifier: Modifier = Modifier,
                      list:MutableList<Dish>,
                      onCheckChanged: (Dish) -> Unit) {


    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement =
        Arrangement.spacedBy(dimensionResource(R.dimen.margin_small))) {
        items(items = list){dish ->
            DishCardComponent(
                checked = dish.checked,
                onCheckChanged = {
                    onCheckChanged(dish)
                    },
                dishTitle = dish.title,
                price = dish.price,
                url = dish.url)

        }
    }
}


@Composable
fun DishCardComponent(modifier: Modifier = Modifier,
                      checked: Boolean,
                      onCheckChanged: () -> Unit,
                      dishTitle:String,
                      price:String,
                      url:String) {


    Surface(color = MaterialTheme.colorScheme.errorContainer,
        shape = ShapeDefaults.Medium) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.margin_small))) {
            Surface (modifier = modifier
                .size(105.dp, 105.dp), shape =
            ShapeDefaults.Medium) {
                AsyncImage(model = url,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds)
            }

            DishDataComponent(modifier = modifier.weight(2.0f),checked = checked,
                onCheckChanged = {onCheckChanged()},
                dishTitle = dishTitle,
                price = price)
        }
    }


    
}


@Composable
fun DishDataComponent(modifier: Modifier = Modifier,
                      checked: Boolean,
                      onCheckChanged: () -> Unit,
                      dishTitle:String,
                      price:String) {

    Column(modifier = modifier
        .padding(start = dimensionResource(R.dimen.margin_extra_small))
        ,horizontalAlignment = Alignment.End,
        ) {
        ReusableTitlePriceContent(dishTitle = dishTitle, price = price)

        AddToBasketComponent(checked = checked) { onCheckChanged()}
    }
    
}

@Composable
fun AddToBasketComponent(modifier: Modifier = Modifier,
                         checked: Boolean,
                         onCheckChanged:(Boolean)->Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.add_to_basket))
        Checkbox(checked = checked, onCheckedChange = {onCheckChanged(it)})
    }


}

@Preview
@Composable
private fun AddToBasketComponentPreview() {

    MaterialTheme{
        Surface {
            AddToBasketComponent(checked = false) {it-> {}}
        }
    }

}

@Preview
@Composable
private fun DishDataComponentPreview() {
    MaterialTheme{
        Surface {
            var check by remember { mutableStateOf(false) }
            DishDataComponent(checked = check,
                onCheckChanged = {check = !check},
                dishTitle = "Title", price = "456")
        }
    }

}

@Preview
@Composable
private fun DishCardComponentPreview() {
    MaterialTheme {
        Surface {
            var check by remember { mutableStateOf(false) }

            DishCardComponent(checked = check,
                onCheckChanged = {check =!check},
                dishTitle = "Title", price = "456",
                url = "https://www.thecocktaildb.com//images//media//drink//5noda61589575158.jpg"
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
            var text by remember{ mutableStateOf("init") }
            Column {
                MenuListComponent(list = list){list->
                    text = list.toString()
                }
                Text(text = text)
            }

        }
    }

}

//@Preview
//@Composable
//private fun MenuScreenPreview() {
//
//    MaterialTheme {
//        Surface {
//            MenuScreen()
//        }
//    }
//
//}