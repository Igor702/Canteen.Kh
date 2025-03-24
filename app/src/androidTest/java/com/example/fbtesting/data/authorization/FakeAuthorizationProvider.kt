package com.example.fbtesting.data.authorization

import android.util.Log
import com.example.fbtesting.TAG
import javax.inject.Inject

class FakeAuthorizationProvider @Inject constructor() : IAuthorizationProvider {
    override val currentUserEmail: String? = FakeAuthorizationProviderTestHelper.getCurrentEmail()

    override fun sendForgotEmail(email: String, onResult: (Throwable?) -> Unit) {
        Log.d(
            TAG, "Provider sendForgotEmail set to null email:$email, onResult $onResult, " +
                    "FakeAuthHelper.getSendForgotResult(): ${FakeAuthorizationProviderTestHelper.getSendForgotResult()}"
        )

        onResult(FakeAuthorizationProviderTestHelper.getSendForgotResult())
    }

    override fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Log.d(TAG, "FakeAuthorizationProvider signInWithEmail," +
                " FakeAuthHelper.getSignInResult(): ${FakeAuthorizationProviderTestHelper.getSignInResult()}"
        )
        onResult(FakeAuthorizationProviderTestHelper.getSignInResult())
    }

    override fun signUpWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        onResult(FakeAuthorizationProviderTestHelper.getSignUpResult())
    }
}


object FakeAuthorizationProviderTestHelper {
    private var sendForgotEmail: Exception? = null
    private var signInWithEmail: Exception? = null
    private var signUpWithEmail: Exception? = null
    private var currentUserEmail:String? = null

    fun setCurrentEmail(email: String){
        currentUserEmail = email
    }
    fun getCurrentEmail():String? = currentUserEmail

    fun sendForgotSuccess(boolean: Boolean) {
        Log.d(TAG, "FakeAuthHelper sendForgotSuccess bool: $boolean")
        if (boolean) {
            sendForgotEmail = null
            Log.d(TAG, "FakeAuthHelper sendForgotEmail set to null:$sendForgotEmail, bool: $boolean")

        } else {
            Log.d(TAG, "FakeAuthHelper sendForgotEmail set to Exception:$sendForgotEmail, bool: $boolean")

            sendForgotEmail = Exception()
        }
    }

    fun getSendForgotResult(): Exception? {
        Log.d(TAG, "FakeAuthHelper getSendForgotResult() sendForgotEmail: $sendForgotEmail")

        return sendForgotEmail
    }


    fun signInSuccess(boolean: Boolean) {
        Log.d(TAG, "FakeAuthHelper signInSuccess signInWithEmail: $signInWithEmail, bool: $boolean")
        if (boolean) {
            //todo: why does it use null, is looks like error, but it isn't. Change in to something more readable
            signInWithEmail = null
        } else {
            signInWithEmail = Exception()
        }
    }

    fun getSignInResult(): Exception? {
        Log.d(TAG, "FakeAuthHelper getSignInResult() signInWithEmail: $signInWithEmail")
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
        currentUserEmail = null
        sendForgotEmail = null
        signInWithEmail = null
        signUpWithEmail = null
    }


}