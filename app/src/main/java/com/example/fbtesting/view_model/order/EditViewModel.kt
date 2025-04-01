package com.example.fbtesting.view_model.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fbtesting.ORDER_STATUS_FALSE
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: IDataRepository
) : ViewModel() {


    private var _editScreenState = MutableStateFlow<EditScreenState>(EditScreenState.Loading)
    val editScreenStatesMap: StateFlow<EditScreenState> get() = _editScreenState

    private var _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> get() = _totalPrice

    private var _lastIndex = MutableStateFlow(-1)

    init {
        getLastIndex()

    }


    //methods related to initializing data

    //fill dishes map depending on id and count totalPrice
    fun initData(listOfIDs: List<String>) {
        val dishes = getMenuData()
        Log.d(TAG, "EditViewModel, listOfIDs: $listOfIDs, dishes: $dishes")

        if (dishes.isNotEmpty()) {
            val map = mutableMapOf<Dish, Int>()
            dishes.forEach { dish ->
                if (listOfIDs.contains(dish.id)) {
                    map[dish] = 1
                    _totalPrice.value += dish.price.toInt()
                }
            }
            Log.d(TAG, "EditViewModel, Success(map:$map)")
            _editScreenState.value = EditScreenState.Success(map)
        }
    }

    private fun getMenuData(): List<Dish> {
        val dishes = mutableListOf<Dish>()
        viewModelScope.launch {
            try {
                val result = repository.getData()
                if (!result.isNullOrEmpty()) {
                    dishes.addAll(result)
                }
            } catch (e: Exception) {
                Log.d(TAG, "EditViewModel, getMenuData, exception: $e")

            }

        }
        return dishes
    }

    private fun getLastIndex() {
        Log.d(TAG, "EditViewModel getLastIndex")
        viewModelScope.launch {
            try {

                _lastIndex.value = repository.getLastIndex()

                Log.d(TAG, "EditViewModel, getLastIndex, lastIndex ${_lastIndex.value}")


            } catch (e: Exception) {
                Log.d(TAG, "EditViewModel, getLastIndex, exception: $e")

            }
        }
    }


    //methods, related to manipulating data
    fun increaseDish(dish: Dish, count: Int) {
        setDishesCount(dish, count)
        increaseTotalPrice(dish.price.toInt())
    }

    fun decreaseDish(dish: Dish, count: Int) {
        setDishesCount(dish, count)
        decreaseTotalPrice(dish.price.toInt())
    }

    private fun setDishesCount(dish: Dish, count: Int) {
        (_editScreenState.value as EditScreenState.Success).map.remove(dish)
        (_editScreenState.value as EditScreenState.Success).map[dish] = count

    }

    private fun increaseTotalPrice(price: Int) {
        _totalPrice.value += price
    }

    private fun decreaseTotalPrice(price: Int) {
        _totalPrice.value -= price
    }


    //methods related with sendOrder flow
    fun sendOrder(totalPrice: String, payBy: String) {
        updateLastIndex()

        (_editScreenState.value as EditScreenState.Success).map.filter { it.value != 0 }
        val order = createNewOrder(totalPrice, payBy)
        if (areNewOrderAndIndexValid(_lastIndex.value.toString(), order)) {
            repository.sendOrder(_lastIndex.value.toString(), order)
            _editScreenState.value = EditScreenState.Navigate(Json.encodeToString(order))

        }
    }

    private fun createNewOrder(totalPrice: String, payBy: String): Order {

        val titleWithCountMap = mutableMapOf<String, Int>()

        (_editScreenState.value as EditScreenState.Success).map.map {
            titleWithCountMap.put(it.key.title, it.value)
        }
        return Order(
            dishes = titleWithCountMap.filter { it.value != 0 } as MutableMap,
            currentUser = repository.getCurrentUserEmail()!!,
            orderStatus = ORDER_STATUS_FALSE,
            totalPrice = totalPrice,
            payBy = payBy
        )

    }

    private fun areNewOrderAndIndexValid(index: String, order: Order): Boolean {

        try {
            index.toInt()

        } catch (e: Exception) {
            return false
        }

        order.apply {
            return !(index.isEmpty() ||
                    dishes.isEmpty() ||
                    currentUser.isEmpty() ||
                    orderStatus.isEmpty() ||
                    totalPrice.isEmpty() ||
                    payBy.isEmpty())
        }


    }

    private fun updateLastIndex() {
        Log.d(TAG, "EditViewModel, updateLastIndex before: ${_lastIndex.value}")

        viewModelScope.launch {
            _lastIndex.value = _lastIndex.value.plus(1)
            Log.d(TAG, "EditViewModel, updateLastIndex after: ${_lastIndex.value}")


        }

    }


}

sealed class EditScreenState {
    data object Loading : EditScreenState()
    data class Success(val map: MutableMap<Dish, Int>) : EditScreenState()
    data class Navigate(val serializedOrder: String) : EditScreenState()
}