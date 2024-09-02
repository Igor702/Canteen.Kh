package com.example.fbtesting.view_model

import android.util.Log
import androidx.lifecycle.*
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data.DataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SharedViewModel @Inject constructor(
    private val repository: DataRepository
): ViewModel() {



    private  var _auth = MutableLiveData(repository.getAuth())
    val auth: LiveData<FirebaseAuth?>get() = _auth

    private var _options = MutableLiveData<List<Dish>?>()
    val options: LiveData<List<Dish>?>get() = _options

    private var _chosenDishes: MutableLiveData<MutableList<Dish?>> = MutableLiveData()
    val chosenDishes: LiveData<MutableList<Dish?>>get() = _chosenDishes

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData()
    val lastIndex: LiveData<Int> get() = _lastIndex


    init {
        getLastIndex()
        setOptions()
    }

    private fun setOptions(){
        viewModelScope.launch {
            try {
                Log.d(TAG, "ViewModel, try block")

                val dbData = repository.getData()
                _options.value = dbData
                Log.d(TAG, "ViewModel, setOptions, dbData: $dbData")
            }catch (e: Exception){
                Log.d(TAG, "ViewModel, setOptions, exception: $e")
            }
        }
    }


    fun setDishes(data: MutableList<Dish?>){
        _chosenDishes.value = data

    }

    private fun getLastIndex(){
         viewModelScope.launch {
             _lastIndex.value = repository.getLastIndex()
         }
    }

    fun sendOrder(index: String, order: Order){
        Log.d(TAG, "ViewModel, sendOrder, index: $index, order: $order")
        repository.sendOrder(index, order)
    }







    class Factory @Inject constructor(myViewModelProvider: Provider<SharedViewModel>
    ) : ViewModelProvider.Factory {

        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            SharedViewModel::class.java to myViewModelProvider
        )

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return (providers[modelClass]!!.get()) as T
        }
    }

    }






