package com.example.fbtesting.view_model.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Dish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: IDataRepository
) : ViewModel() {

    private var _menuData = MutableStateFlow<MenuScreenState>(MenuScreenState.Loading).also {
        Log.d(TAG, "MenuViewModel, also, _menuData: ${it.value} ")
    }
    val menuData: StateFlow<MenuScreenState> get() = _menuData

    private var _modifiedList = MutableStateFlow(mutableListOf<Dish>())

    private var _restoreState = MutableStateFlow(false)
    //todo: make property to store loadedDishes

    init {
        loadMenuData()
    }

    private fun loadMenuData() {

        if (_menuData.value == MenuScreenState.Loading) {
            viewModelScope.launch {

                val result = repository.getData()
                Log.d(TAG, "MenuViewModel, loadMenuData, result: $result")

                if (!result.isNullOrEmpty()) {
                    _menuData.value = MenuScreenState.LoadSuccess(result)
                    Log.d(TAG, "MenuViewModel, loadMenuData, _menuData: ${_menuData.value}")


                } else {
                    Log.d(
                        TAG,
                        "MenuViewModel, loadMenuData error before, _menuData: ${_menuData.value}"
                    )

                    _menuData.value = MenuScreenState.LoadError("")

                    Log.d(
                        TAG,
                        "MenuViewModel, loadMenuData error after, _menuData: ${_menuData.value}"
                    )

                }

            }
        }

    }

    fun setModifiedList(data: List<Dish>){
        _modifiedList.value.addAll(data)
        _restoreState.value = true

        _menuData.value = MenuScreenState.Navigate( data.filter { it.checked }.map { it.id })
        Log.d(TAG, "MenuViewModel, setModifiedList, data: $data, list: ${_modifiedList.value}, restoreState: ${_restoreState.value}")


    }

    fun restoreStateIfHas(){
        Log.d(TAG, "MenuViewModel, restoreStateIfHas")
        if (_restoreState.value){
            Log.d(TAG, "MenuViewModel, restoreStateIfHas, restore")
            _menuData.value = MenuScreenState.LoadSuccess(_modifiedList.value)
        }
    }



//    fun setDishes(data: List<Dish>) {
//
//        for (i in data) {
//            if (i.checked) {
//                Log.d(TAG, "ViewModel, setDished, chosenDishes checked dish:${i}")
//
//                _chosenDishes.value.add(i)
//                Log.d(TAG, "ViewModel, setDished, chosenDishes add:${_chosenDishes.value.size}")
//
//            }
//        }
//
//
//        Log.d(
//            TAG,
//            "ViewModel, setDished, chosenDishes:${_chosenDishes.value.size}, viewModel: ${this.hashCode()}"
//        )
//        data.map { it.checked } //make this instead all!!!
//        if (_chosenDishes.value.isNotEmpty()) {
//            _menuData.value = MenuScreenState.Navigate(_chosenDishes.value.extractIDs())
//        }
//
//
//    }
//
//    fun restoreState(){
//        _menuData.value = MenuScreenState.LoadSuccess
//    }

//
//    private fun Set<Dish>.extractIDs(): List<String> {
//        val resultList = mutableListOf<String>()
//        this.toList().forEach {
//            resultList.add(it.id)
//        }
//        return resultList
//    }
}

sealed class MenuScreenState {
    data object Loading : MenuScreenState()
    data class LoadSuccess(val list: List<Dish>) : MenuScreenState()
    data class LoadError(val error: String) : MenuScreenState()
    data class Navigate(val list: List<String>) : MenuScreenState()
}