package com.example.fbtesting.view_model

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.ORDER_READY
import com.example.fbtesting.ORDER_STATUS_FALSE
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.convertOrderToString
import com.example.fbtesting.data_models.toOrder
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: IDataRepository,
) : ViewModel() {


    private var _currentUserEmail = MutableStateFlow(repository.getCurrentUserEmail())
    val currentUserEmail = _currentUserEmail.asStateFlow()


    private var _menuData = MutableStateFlow<MenuDataState>(MenuDataState.Loading)
    val menuData: StateFlow<MenuDataState> get() = _menuData


    private var _chosenDishes = MutableStateFlow(mutableSetOf<Dish>())


    private var _lastIndex = MutableStateFlow(-1)
    val lastIndex = _lastIndex.asStateFlow()


    private val _dishesWithCountMap = MutableStateFlow(mutableMapOf<String, Int>())


    private val _sentOrder = MutableStateFlow(Order())


    private val _sentIndex = MutableStateFlow(0)


    private val _orderStatus = MutableStateFlow(ORDER_COOKING)
    val orderStatus = _orderStatus.asStateFlow()


    private val orderRef = Firebase.database.getReference("orders")

    init {
        loadMenuData()
    }


    private fun onOrderStatusChangedListener() {
        //should be located in repo for better testing approach
        orderRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "child added: ${snapshot.value}")
                _orderStatus.value = ORDER_COOKING + _sentIndex.value.toString()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                val temp = checkWhoseOrder(getOrder(snapshot), currentUserEmail.value)
                Log.d(TAG, "child changed: $temp")
                if (temp) {

                    viewModelScope.launch {
                        Log.d(TAG, "onChildChanged, status before: ${_orderStatus.value}")
                        _orderStatus.value = ORDER_READY + _sentIndex.value.toString()
                        Log.d(TAG, "onChildChanged, status after: ${_orderStatus.value}")


                    }


                }


            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(TAG, "child removed: ${snapshot.value}")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "child moved: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "child error: $error")
            }

        })
    }


    private fun getOrder(snapshot: DataSnapshot): Order {
        val changedOrderMap = snapshot.value as HashMap<*, *>
        return changedOrderMap.toOrder()
    }


    private fun checkWhoseOrder(order: Order, currentUserEmail: String?): Boolean {
        Log.d(
            TAG,
            "checkOrder order: ${order.currentUser}, auth.user: $currentUserEmail "
        )
        if (order.currentUser == currentUserEmail) {
            Log.d(TAG, "checkWhoseOrder: true")
            return true
        }
        Log.d(TAG, "checkWhoseOrder: false")

        return false
    }


    fun getStatusOrderDishesWithCountString(): String {
        return _sentOrder.value.dishes.convertOrderToString()
    }

    fun getStatusOrderTotalPrice(): String {
        return _sentOrder.value.totalPrice
    }

    fun getStatusPayBy(): String {
        return _sentOrder.value.payBy

    }


    fun getChosenDishes(): SnapshotStateList<Dish> {
        fillDishesWithCountMap(_chosenDishes.value)
        Log.d(TAG, "ViewModel, getChosenDishes,${_chosenDishes.value.toList()}")
        return _chosenDishes.value.toMutableStateList()
    }

    private fun fillDishesWithCountMap(chosenDishes: MutableSet<Dish>) {
        Log.d(TAG, "ViewModel, fillChosenDishes,chosenDishes: $chosenDishes")

        chosenDishes.forEach { dish: Dish ->
            Log.d(
                TAG,
                "ViewModel, fillChosenDishes, forEach, dish: $dish,  dishesWithCountMap:${_dishesWithCountMap.value}"
            )

            _dishesWithCountMap.value[dish.title] = 1
        }
        Log.d(TAG, "ViewModel, fillChosenDishes, dishesWithCountMap:${_dishesWithCountMap.value}")


    }

    fun setDishesCount(dishTitle: String, count: Int) {
        Log.d(TAG, "ViewModel, setDishesCount before,${_dishesWithCountMap.value}")
        _dishesWithCountMap.value.remove(dishTitle)
        _dishesWithCountMap.value[dishTitle] = count
        Log.d(TAG, "ViewModel, setDishesCount after,${_dishesWithCountMap.value}")

    }

    fun getInitPrice(): Int {
        var total = 0
        _chosenDishes.value.forEach(
            action = { dish: Dish ->
                total += dish.price.toInt()
            }
        )
        return total
    }


    private fun loadMenuData() {

        if (_menuData.value == MenuDataState.Loading) {
            viewModelScope.launch {

                val result = repository.getData()
                Log.d(TAG, "ViewModel, loadMenuData, result: $result")

                if (!result.isNullOrEmpty()) {
                    _menuData.value = MenuDataState.Success(result.toMutableStateList())
//                menuData.addAll(_menuData.value)
                    Log.d(TAG, "ViewModel, loadMenuData, _menuData: ${_menuData.value}")


                } else {
                    Log.d(
                        TAG,
                        "ViewModel, loadMenuData error before, _menuData: ${_menuData.value}"
                    )

                    _menuData.value = MenuDataState.Error("")

                    Log.d(TAG, "ViewModel, loadMenuData error after, _menuData: ${_menuData.value}")

                }

            }
        }

    }

    fun setDishes(data: List<Dish>): Boolean {
        getLastIndex()

        for (i in data) {
            if (i.checked) {
                Log.d(TAG, "ViewModel, setDished, chosenDishes checked dish:${i}")

                _chosenDishes.value.add(i)
                Log.d(TAG, "ViewModel, setDished, chosenDishes add:${_chosenDishes.value.size}")

            }
        }


        Log.d(
            TAG,
            "ViewModel, setDished, chosenDishes:${_chosenDishes.value.size}, viewModel: ${this.hashCode()}"
        )
        return _chosenDishes.value.isNotEmpty()

    }

    private fun getLastIndex() {
        Log.d(TAG, "SharedViewModel getLastIndex")
        viewModelScope.launch {
            try {

                _lastIndex.value = repository.getLastIndex()

                Log.d(TAG, "ViewModel, getLastIndex, lastIndex ${_lastIndex.value}")


            } catch (e: Exception) {
                Log.d(TAG, "ViewModel, getLastIndex, exception: $e")
            }
        }
    }


    private fun checkDishesCountAndRemoveZero(dishes: MutableMap<String, Int>): MutableMap<String, Int> {
        for (i in _chosenDishes.value.toList()) {
            if (dishes[i.title] == 0) {
                dishes.remove(i.title)
            }
        }

        Log.d(TAG, "SummaryFragment, checkDishesCountAndRemoveZero, dishes $dishes")
        return dishes
    }

    fun sendOrder(totalPrice: String, payBy: String): Boolean {

        Log.d(TAG, "ViewModel, sendOrder, index: ${lastIndex.value}, ${_dishesWithCountMap.value}")
        updateLastIndex()
        checkDishesCountAndRemoveZero(_dishesWithCountMap.value)
        val order = createNewOrder(totalPrice, payBy)
        if (areNewOrderAndIndexValid(lastIndex.value.toString(), order)) {
            Log.d(
                TAG,
                "ViewModel, sendOrder, repo.sendOrder: ${lastIndex.value}index, order: $order"
            )

            repository.sendOrder(lastIndex.value.toString(), order)
            _sentOrder.value = order
            _sentIndex.value = lastIndex.value
            onOrderStatusChangedListener()


            return true
        } else {
            return false
        }
    }

    private fun createNewOrder(totalPrice: String, payBy: String): Order {

        return Order(
            dishes = _dishesWithCountMap.value,
            currentUser = currentUserEmail.value!!,
            orderStatus = ORDER_STATUS_FALSE,
            totalPrice = totalPrice,
            payBy = payBy
        )

    }

    private fun updateLastIndex() {
        Log.d(TAG, "ViewModel, updateLastIndex before: ${_lastIndex.value}")

        viewModelScope.launch {
            _lastIndex.value = _lastIndex.value.plus(1)
            Log.d(TAG, "ViewModel, updateLastIndex after: ${_lastIndex.value}")


        }

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


}

sealed class MenuDataState {
    data object Loading : MenuDataState()
    data class Success(val list: SnapshotStateList<Dish>) : MenuDataState()
    data class Error(val error: String) : MenuDataState()
}






