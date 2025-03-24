package com.example.fbtesting.view_model.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fbtesting.TAG
import com.example.fbtesting.data.authorization.IAuthorizationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authProvider: IAuthorizationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()


    fun singInWithEmailAndPassword(email: String, password: String) {
        authProvider.signInWithEmail(email, password, onResult = { exception ->

            if (exception == null) {
                _uiState.value = UiState.Success
                Log.d(
                    TAG,
                    "SignInViewModel, signInWithEmailAndPass, success, uiState:${_uiState.value}"
                )


            } else {
                _uiState.value = UiState.Error(exception.message.toString())
                Log.d(TAG, "SignInViewModel, signInWithEmailAndPass, error:${exception.message} ")

            }


        })
    }


}