package com.example.fbtesting.view_model

import android.util.Log
import androidx.lifecycle.*
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SharedViewModel @Inject constructor(
    private val repository: IDataRepository
): ViewModel() {



    private  var _auth = MutableLiveData(repository.getAuth())
    val auth: LiveData<FirebaseAuth?>get() = _auth

    private var _menuData = MutableLiveData<List<Dish>?>()
    val menuData: LiveData<List<Dish>?>get() = _menuData

    private var _chosenDishes: MutableLiveData<MutableList<Dish?>> = MutableLiveData()
    val chosenDishes: LiveData<MutableList<Dish?>>get() = _chosenDishes

    private var _lastIndex: MutableLiveData<Int> = MutableLiveData()
    val lastIndex: LiveData<Int> get() = _lastIndex



    fun loadMenuData(){
        viewModelScope.launch {
            try {
                Log.d(TAG, "ViewModel, try block")

                val data = repository.getData()
                _menuData.value = data
                Log.d(TAG, "ViewModel, setOptions, dbData: $data")
            }catch (e: Exception){
                Log.d(TAG, "ViewModel, setOptions, exception: $e")
            }
        }
    }


    fun setDishes(data: MutableList<Dish?>){
        _chosenDishes.value = data

    }

    fun getLastIndex(){
        Log.d(TAG, "SharedViewModel getLastIndex")
         viewModelScope.launch {
             try {
                 _lastIndex.value = repository.getLastIndex()
             }catch (e: Exception){
                 Log.d(TAG, "ViewModel, getLastIndex, exception: $e")
             }
         }
    }

    fun sendOrder(index: String, order: Order):Boolean{
        Log.d(TAG, "ViewModel, sendOrder, index: $index, order: $order")
        if (areNewOrderAndIndexValid(index, order)){
            repository.sendOrder(index, order)
            return true
        }else{
            return false
        }
    }

    private fun areNewOrderAndIndexValid(index: String,order: Order):Boolean{

        try {
            index.toInt()
        }catch (e:Exception){
            return false
        }

        order.apply {
            return !(index.isEmpty()||
                    dishes.isEmpty()||
                    currentUser.isEmpty()||
                    orderStatus.isEmpty()||
                    totalPrice.isEmpty()||
                    payBy.isEmpty())
        }


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






