package com.example.fbtesting.view_model.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.ORDER_READY
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val repository: IDataRepository
) : ViewModel() {

    private var _lastIndex = MutableStateFlow(-1)

    private val _cookingStatus = MutableStateFlow(ORDER_COOKING + _lastIndex.value.toString())
    val cookingStatus: StateFlow<String> = _cookingStatus.asStateFlow()


    private val _order = MutableStateFlow<Order>(Order())
    val order: StateFlow<Order> get() = _order

    init {
        Log.d(TAG, "StatusViewModel, init")
        getLastIndex()
        onOrderStatusChangedListener()
    }

    private fun onOrderStatusChangedListener() {
        viewModelScope.launch {
            repository.onOrderStatusChangedListener().collect {
                when (it) {
                    ORDER_READY -> {
                        _cookingStatus.value = ORDER_READY + _lastIndex.value.toString()

                    }

                    ORDER_COOKING -> {
                        _cookingStatus.value = ORDER_COOKING + _lastIndex.value.toString()

                    }

                }

            }

        }

    }


    private fun getLastIndex() {
        Log.d(TAG, "StatusViewModel getLastIndex")
        viewModelScope.launch {
            try {

                _lastIndex.value = repository.getLastIndex()

                Log.d(TAG, "StatusViewModel, getLastIndex, lastIndex ${_lastIndex.value}")

                _cookingStatus.value = ORDER_COOKING + _lastIndex.value.toString()


            } catch (e: Exception) {
                Log.d(TAG, "StatusViewModel, getLastIndex, exception: $e")
            }
        }
    }

    fun setSerializedOrder(serializedOrder: String) {
        Log.d(TAG, "StatusViewModel, setSerializedOrder, serializedOrder: $serializedOrder")

        _order.value = Json.decodeFromString<Order>(serializedOrder)

        Log.d(TAG, "StatusViewModel, setSerializedOrder, after")

    }


}
