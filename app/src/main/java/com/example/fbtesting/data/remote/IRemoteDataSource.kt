package com.example.fbtesting.data.remote

import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import com.google.firebase.auth.FirebaseAuth

interface IRemoteDataSource {
    fun getFirebaseAuth(): FirebaseAuth?
    suspend fun getMenuData(): List<Dish>
    fun sendOrder(index: String, order: Order)
    suspend fun getIndex(): Int
}