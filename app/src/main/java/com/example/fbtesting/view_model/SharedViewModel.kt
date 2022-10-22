package com.example.fbtesting.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.model.DataRepository
import com.example.fbtesting.model.TAG
import com.example.fbtesting.models.Dish
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SharedViewModel(val repository: DataRepository = DataRepository().getRepository()): ViewModel() {

private  var _auth = MutableLiveData(repository.getAuth())
    val auth: LiveData<FirebaseAuth>get() = _auth

    private var _options = MutableLiveData(repository.getOptions())
    val options: LiveData<FirebaseRecyclerOptions<Dish>>get() = _options

    private var _chosenDishes: MutableLiveData<MutableList<Dish?>> = MutableLiveData()
    val chosenDishes: LiveData<MutableList<Dish?>>get() = _chosenDishes

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData()
    val lastIndex: LiveData<Int> get() = _lastIndex

    init {
        getLastIndex()
        Log.d(TAG, "init block")
    }



    fun setDishes(data: MutableList<Dish?>){
        _chosenDishes.value = data

    }

    private fun getLastIndex(){
         viewModelScope.launch {
             Log.d(TAG, "ViewModel, getLastIndex, index: deleted    ")
             _lastIndex.value = repository.getLastIndex()
         }
    }

    fun sendOrder(index: String, order: Order){
        Log.d(TAG, "ViewModel, sendOrder, index: $index, order: $order")
        repository.sendOrder(index, order)
    }


    }






