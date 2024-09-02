package com.example.fbtesting.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

// Interface representing Firebase Authentication functionality
interface FirebaseAuthInterface {
    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult>
}

// Your FirebaseAuthentication class that depends on FirebaseAuth
class FirebaseAuthentication(private val firebaseAuth: FirebaseAuthInterface) {
    fun signIn(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }
}