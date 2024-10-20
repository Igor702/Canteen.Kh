package com.example.fbtesting.view_model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    //todo: in must be a set, not a list
    private var _chosenDishes = MutableLiveData(mutableListOf<Dish>())
//    val chosenDishes: SnapshotStateList<Dish> get() = _chosenDishes

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData()
    val lastIndex: LiveData<Int> get() = _lastIndex

    private val _dishesWithCountMap = MutableLiveData<MutableMap<String, Int>>()


    fun getChosenDishes():SnapshotStateList<Dish>{
        fillDishesWithCountMap(_chosenDishes)
        Log.d(TAG, "ViewModel, getChosenDishes,${_chosenDishes.value?.toList()}")
        return _chosenDishes.value!!.toMutableStateList()
    }

    private fun fillDishesWithCountMap(chosenDishes: MutableLiveData<MutableList<Dish>>) {
        Log.d(TAG, "ViewModel, fillChosenDishes,chosenDishes: ${chosenDishes.value}")

        chosenDishes.value?.forEach { dish: Dish ->
            _dishesWithCountMap.value?.put(dish.title, 1)
        }
        Log.d(TAG, "ViewModel, fillChosenDishes,${_dishesWithCountMap}")


    }

    fun setDishesCount(dishTitle:String, count:Int){
        Log.d(TAG, "ViewModel, setDishesCount before,${_dishesWithCountMap}")
        _dishesWithCountMap.value?.remove(dishTitle)
        _dishesWithCountMap.value?.put(dishTitle, count)
        Log.d(TAG, "ViewModel, setDishesCount after,${_dishesWithCountMap}")

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

    //todo solve the problem here
    fun setDishes(data: List<Dish>):Boolean {
        Log.d(TAG, "ViewModel, setDished data list:$data")

        for (i in data){
            Log.d(TAG, "ViewModel, setDished, dish: ${i}")


            if (i.checked){
                Log.d(TAG, "ViewModel, setDished, chosenDishes checked dish:${i}")

                _chosenDishes.value?.add(i)
                Log.d(TAG, "ViewModel, setDished, chosenDishes add:${_chosenDishes.value?.size}")

            }
        }

//        data.forEach {dish: Dish ->
//            if (dish.checked){
//                Log.d(TAG, "ViewModel, setDished, chosenDishes add:${_chosenDishes.value?.size}")
//                _chosenDishes.value?.add(dish)
//            }
//        }
        Log.d(TAG, "ViewModel, setDished, chosenDishes:${_chosenDishes.value?.size}, viewModel: ${this.hashCode()}")
        return !_chosenDishes.value.isNullOrEmpty()

    }

    fun getLastIndex() {
        Log.d(TAG, "SharedViewModel getLastIndex")
        viewModelScope.launch {
            try {
                _lastIndex.value = repository.getLastIndex()
            } catch (e: Exception) {
                Log.d(TAG, "ViewModel, getLastIndex, exception: $e")
            }
        }
    }

    fun sendOrder(index: String = lastIndex.value.toString(), order: Order): Boolean {



        Log.d(TAG, "ViewModel, sendOrder, index: $index, order: $order")
        if (areNewOrderAndIndexValid(index, order)) {
//            repository.sendOrder(index, order)
            Log.d(TAG, "ViewModel, sendOrder, repo.sendOrder: $index, order: $order")

            return true
        } else {
            return false
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






