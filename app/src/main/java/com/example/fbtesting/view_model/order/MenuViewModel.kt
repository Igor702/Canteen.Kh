package com.example.fbtesting.view_model.order

import androidx.lifecycle.ViewModel
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.view_model.MenuDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//uiState: Loading, Success, Error, Navigate

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: IDataRepository
) : ViewModel() {

    private var _currentUserEmail = MutableStateFlow(repository.getCurrentUserEmail())
    val currentUserEmail = _currentUserEmail.asStateFlow()


    private var _menuData = MutableStateFlow<MenuDataState>(MenuDataState.Loading)
    val menuData: StateFlow<MenuDataState> get() = _menuData
}

//sealed class MenuDataState {
//    data object Loading : MenuDataState()
//    data class Success(val list: SnapshotStateList<Dish>) : MenuDataState()
//    data class Error(val error: String) : MenuDataState()
//}