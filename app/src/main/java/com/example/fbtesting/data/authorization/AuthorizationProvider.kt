package com.example.fbtesting.data.authorization

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


class AuthorizationProvider @Inject constructor() : IAuthorizationProvider {

    //when operation made without exception and return null - it mean success

    override val currentUserEmail: String = "${Firebase.auth.currentUser}"


    override fun sendForgotEmail(email: String, onResult: (Throwable?) -> Unit) {

        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                onResult(it.exception)
            }
    }

    override fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        singInWithEmailAndPassword(email, password, onResult = onResult)

    }

    private fun singInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.exception)
            }

    }

    override fun signUpWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.exception)

            }
    }
}



