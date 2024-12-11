package com.example.fbtesting.data.authorization

import android.util.Log
import com.example.fbtesting.TAG
import javax.inject.Inject

class FakeAuthorizationProvider @Inject constructor() : IAuthorizationProvider {
    override fun sendForgotEmail(email: String, onResult: (Throwable?) -> Unit) {
        Log.d(
            TAG, "Provider sendForgotEmail set to null email:$email, onResult $onResult, " +
                    "Helper.getSendForgotResult(): ${FakeProviderTestHelper.getSendForgotResult()}"
        )

        onResult(FakeProviderTestHelper.getSendForgotResult())
    }

    override fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        onResult(FakeProviderTestHelper.getSignInResult())
    }

    override fun signUpWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        onResult(FakeProviderTestHelper.getSignUpResult())
    }
}


object FakeProviderTestHelper {
    private var sendForgotEmail: Exception? = null
    private var signInWithEmail: Exception? = null
    private var signUpWithEmail: Exception? = null

    fun sendForgotSuccess(boolean: Boolean) {
        Log.d(TAG, "Helper sendForgotSuccess bool: $boolean")
        if (boolean) {
            sendForgotEmail = null
            Log.d(TAG, "Helper sendForgotEmail set to null:$sendForgotEmail, bool: $boolean")

        } else {
            Log.d(TAG, "Helper sendForgotEmail set to Exception:$sendForgotEmail, bool: $boolean")

            sendForgotEmail = Exception()
        }
    }

    fun getSendForgotResult(): Exception? {
        Log.d(TAG, "Helper getSendForgotResult() sendForgotEmail: $sendForgotEmail")

        return sendForgotEmail
    }


    fun signInSuccess(boolean: Boolean) {
        if (boolean) {
            signInWithEmail = null
        } else {
            signInWithEmail = Exception()
        }
    }

    fun getSignInResult(): Exception? {
        return signInWithEmail
    }

    fun signUpSuccess(boolean: Boolean) {
        if (boolean) {
            signUpWithEmail = null
        } else {
            signUpWithEmail = Exception()
        }
    }

    fun getSignUpResult(): Exception? {
        return signUpWithEmail
    }


    fun reset() {
        sendForgotEmail = null
        signInWithEmail = null
        signUpWithEmail = null
    }


}