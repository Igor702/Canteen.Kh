package com.example.fbtesting.data.authorization

interface IAuthorizationProvider {
    val currentUserEmail: String?

    fun sendForgotEmail(email: String, onResult: (Throwable?) -> Unit)

    fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit)

    fun signUpWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit)


}