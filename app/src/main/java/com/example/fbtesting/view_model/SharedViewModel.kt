package com.example.fbtesting.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: IDataRepository
) : ViewModel() {


    private var _currentUserEmail = MutableLiveData(repository.getCurrentUserEmail())
    val currentUserEmail: LiveData<String?> get() = _currentUserEmail

    private var _menuData = mutableStateListOf<Dish>().also {
        loadMenuData()
    }
    val menuData:SnapshotStateList<Dish> get()= _menuData


    private var _chosenDishes = mutableStateListOf<Dish>()
    val chosenDishes: SnapshotStateList<Dish> get() = _chosenDishes

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData()
    val lastIndex: LiveData<Int> get() = _lastIndex



    fun loadMenuData() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "ViewModel, try block")

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


    fun setDishes(data: SnapshotStateList<Dish>):Boolean {
        data.forEach {dish: Dish ->
            if (dish.checked){
                _chosenDishes.add(dish)
            }
        }
        return _chosenDishes.size != 0

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

    fun sendOrder(index: String, order: Order): Boolean {
        Log.d(TAG, "ViewModel, sendOrder, index: $index, order: $order")
        if (areNewOrderAndIndexValid(index, order)) {
            repository.sendOrder(index, order)
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






