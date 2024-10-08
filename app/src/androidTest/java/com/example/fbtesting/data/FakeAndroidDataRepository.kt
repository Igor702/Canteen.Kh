package com.example.fbtesting.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FakeAndroidDataRepository @Inject constructor():IDataRepository {


    override fun getCurrentUserEmail(): String? {
        val email = FakeAndroidDataLoader.getCurrentUserEmail()
        Log.d(TAG, "FakeAndroidDataRepository, getUserEmail:$email")
        return email
    }

    override suspend fun getData(): List<Dish>? {
        return FakeAndroidDataLoader.getData()
    }


    //don't do this
    override suspend fun getLastIndex(): Int {
        return FakeAndroidDataLoader.getLastIndex()
    }

    override fun sendOrder(index: String, order: Order) {
        FakeAndroidDataLoader.sendOrder(index, order)
    }



}