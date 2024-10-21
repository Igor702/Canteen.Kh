package com.example.fbtesting.view_model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.fbtesting.ORDER_STATUS_FALSE
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: IDataRepository,
) : ViewModel() {


    private var _currentUserEmail = MutableLiveData(repository.getCurrentUserEmail())
    val currentUserEmail: LiveData<String?> get() = _currentUserEmail

    private var _menuData = mutableStateListOf<Dish>().also {
        loadMenuData()

    }

    val menuData:SnapshotStateList<Dish> get()= _menuData


    private var _chosenDishes = MutableLiveData(mutableSetOf<Dish>())

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData<Int>()

    val lastIndex: LiveData<Int> get() = _lastIndex

    private val _dishesWithCountMap = MutableLiveData(mutableMapOf<String, Int>())


    fun getChosenDishes():SnapshotStateList<Dish>{
        fillDishesWithCountMap(_chosenDishes)
        Log.d(TAG, "ViewModel, getChosenDishes,${_chosenDishes.value?.toList()}")
        return _chosenDishes.value!!.toMutableStateList()
    }

    private fun fillDishesWithCountMap(chosenDishes: MutableLiveData<MutableSet<Dish>>) {
        Log.d(TAG, "ViewModel, fillChosenDishes,chosenDishes: ${chosenDishes.value}")

        chosenDishes.value?.forEach { dish: Dish ->
            Log.d(TAG, "ViewModel, fillChosenDishes, forEach, dish: $dish,  dishesWithCountMap:${_dishesWithCountMap.value}")

            _dishesWithCountMap.value?.put(dish.title, 1)
        }
        Log.d(TAG, "ViewModel, fillChosenDishes, dishesWithCountMap:${_dishesWithCountMap.value}")


    }

    fun setDishesCount(dishTitle:String, count:Int){
        Log.d(TAG, "ViewModel, setDishesCount before,${_dishesWithCountMap.value}")
        _dishesWithCountMap.value?.remove(dishTitle)
        _dishesWithCountMap.value?.put(dishTitle, count)
        Log.d(TAG, "ViewModel, setDishesCount after,${_dishesWithCountMap.value}")

    }

     fun getInitPrice():Int{
        var total: Int = 0
        _chosenDishes.value!!.forEach(
            action = {dish: Dish ->
                total +=dish.price.toInt()
            }
        )
        return total
    }


    fun loadMenuData() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "ViewModel, try block, viewModel: ${this.hashCode()}")

                val data = repository.getData()
                if (data!=null){
                    _menuData.addAll(data)

                }
                Log.d(TAG, "ViewModel, setOptions, dbData: $data")
            } catch (e: Exception) {
                Log.d(TAG, "ViewModel, setOptions, exception: $e")
            }
        }
    }

    fun setDishes(data: List<Dish>):Boolean {
        getLastIndex()

        for (i in data){
            if (i.checked){
                Log.d(TAG, "ViewModel, setDished, chosenDishes checked dish:${i}")

                _chosenDishes.value?.add(i)
                Log.d(TAG, "ViewModel, setDished, chosenDishes add:${_chosenDishes.value?.size}")

            }
        }


        Log.d(TAG, "ViewModel, setDished, chosenDishes:${_chosenDishes.value?.size}, viewModel: ${this.hashCode()}")
        return !_chosenDishes.value.isNullOrEmpty()

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
        for (i in _chosenDishes.value!!.toList()) {
            if (dishes[i.title] == 0) {
                dishes.remove(i.title)
            }
        }

        Log.d(TAG, "SummaryFragment, checkDishesCountAndRemoveZero, dishes $dishes")
        return dishes
    }

    fun sendOrder( totalPrice:String, payBy:String): Boolean {

        Log.d(TAG, "ViewModel, sendOrder, index: ${lastIndex.value}, ${_dishesWithCountMap.value}")
        updateLastIndex()
        checkDishesCountAndRemoveZero(_dishesWithCountMap.value!!)
        val order = createNewOrder(totalPrice, payBy)
        if (areNewOrderAndIndexValid(lastIndex.value.toString(), order)) {
            Log.d(TAG, "ViewModel, sendOrder, repo.sendOrder: ${lastIndex.value}index, order: $order")

            repository.sendOrder(lastIndex.value.toString(), order)

            return true
        } else {
            return false
        }
    }

    private fun createNewOrder(totalPrice: String, payBy: String):Order {

        return Order(
            dishes = _dishesWithCountMap.value!!,
            currentUser = currentUserEmail.value!!,
            orderStatus = ORDER_STATUS_FALSE,
            totalPrice = totalPrice,
            payBy = payBy
        )

    }

    private fun updateLastIndex() {
        Log.d(TAG, "ViewModel, updateLastIndex before: ${_lastIndex.value}")

        viewModelScope.launch {
            _lastIndex.value = _lastIndex.value?.plus(1)
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






