package com.example.fbtesting.view_model.authorization

import androidx.lifecycle.ViewModel
import com.example.fbtesting.data.authorization.IAuthorizationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val authProvider: IAuthorizationProvider
) : ViewModel() {

    private val _isEmailSent = MutableStateFlow(false)
    val isEmailSent = _isEmailSent.asStateFlow()


    fun sendForgotEmail(email: String) {
        authProvider.sendForgotEmail(email, onResult = { exception ->

            if (exception == null) {
                _isEmailSent.value = true

            }


        })
    }


}