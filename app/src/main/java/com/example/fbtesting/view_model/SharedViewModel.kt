package com.example.fbtesting.view_model

import android.util.Log
import androidx.lifecycle.*
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.model.DataRepository
import com.example.fbtesting.model.TAG
import com.example.fbtesting.models.Dish
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class SharedViewModel @Inject constructor(
    val repository: DataRepository
//    val repository: DataRepository = DataRepository().getRepository()
): ViewModel() {

private  var _auth = MutableLiveData(repository.getAuth())
    val auth: LiveData<FirebaseAuth>get() = _auth


    //list of dishes for MenuFragment
    private var _options = MutableLiveData<List<Dish>>()
    val options: LiveData<List<Dish>>get() = _options

    private var _chosenDishes: MutableLiveData<MutableList<Dish?>> = MutableLiveData()
    val chosenDishes: LiveData<MutableList<Dish?>>get() = _chosenDishes

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData()
    val lastIndex: LiveData<Int> get() = _lastIndex

    init {

//        (application as )
        Log.d(TAG, "init block0")

        getLastIndex()
        Log.d(TAG, "init block1")

        setOptions()
        Log.d(TAG, "init block2")

    }

    private fun setOptions(){

        viewModelScope.launch {
            try {
                Log.d(TAG, "ViewModel, try block")
//              val  temp =  repository.getLocalData()
//                if (temp!= null){
//                    _options.value  = temp!!
//                }else{
//                    _options.value = repository.getOptions().snapshots.toList().map {
//                        it as Dish
//                    }
//                }
//todo: it's part of code working, modify code everywhere for working with usual list of Dishes
                val temp = repository.getOptions()
                _options.value = temp
                //_options.value = temp
                Log.d(TAG, "ViewModel, setOptions, temp: $temp")
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
             Log.d(TAG, "ViewModel, getLastIndex, index: deleted    ")
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






