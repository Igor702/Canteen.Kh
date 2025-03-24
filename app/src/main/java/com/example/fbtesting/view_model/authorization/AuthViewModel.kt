package com.example.fbtesting.view_model.authorization

import androidx.lifecycle.ViewModel
import com.example.fbtesting.data.IDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    repository: IDataRepository
) : ViewModel() {

    private var _currentUserEmail = MutableStateFlow(repository.getCurrentUserEmail())
    val currentUserEmail = _currentUserEmail.asStateFlow()
}