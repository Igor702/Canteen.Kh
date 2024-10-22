package com.example.fbtesting.data.remote

import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order

interface IRemoteDataSource {
    fun getCurrentUserEmail(): String?
    suspend fun getMenuData(): List<Dish>
    fun sendOrder(index: String, order: Order)
    suspend fun getIndex(): Int
}