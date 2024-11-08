package com.example.fbtesting.view_model.authorization

import androidx.lifecycle.ViewModel
import com.example.fbtesting.data.authorization.IAuthorizationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authProvider: IAuthorizationProvider
) : ViewModel() {

    private val _isSigned = MutableStateFlow(false)
    val isSigned = _isSigned.asStateFlow()


    fun singInWithEmailAndPassword(email: String, password: String) {
        authProvider.signInWithEmail(email, password, onResult = { exception ->

            if (exception == null) {
                _isSigned.value = true

            }


        })
    }


}