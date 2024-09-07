package com.example.fbtesting.data

import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth

interface IDataRepository {


    fun getUserEmail():String?

    suspend fun getData():List<Dish>?
    suspend fun getLastIndex():Int

    fun sendOrder(index: String, order: Order)

}
