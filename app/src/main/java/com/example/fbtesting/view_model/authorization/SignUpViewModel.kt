package com.example.fbtesting.view_model.authorization

import androidx.lifecycle.ViewModel
import com.example.fbtesting.data.authorization.IAuthorizationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authProvider: IAuthorizationProvider
) : ViewModel() {

    private val _isSignUpComplete = MutableStateFlow(false)
    val isSignUpComplete = _isSignUpComplete.asStateFlow()


    fun singUpWithEmailAndPassword(email: String, password: String) {
        authProvider.signUpWithEmail(email, password, onResult = { exception ->

            if (exception == null) {
                _isSignUpComplete.value = true
            }

        })
    }


}