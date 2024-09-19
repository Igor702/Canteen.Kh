package com.example.fbtesting.data

import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import com.google.firebase.auth.FirebaseAuth

interface IDataRepository {

    fun getCurrentUserEmail(): String?
    suspend fun getData(): List<Dish>?
    suspend fun getLastIndex(): Int

    fun sendOrder(index: String, order: Order)

}
