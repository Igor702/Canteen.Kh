package com.example.fbtesting.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SharedViewModel: ViewModel() {

private  var _auth = MutableLiveData(Firebase.auth)
    val auth: LiveData<FirebaseAuth>get() = _auth


    }






