package com.example.fbtesting.data.local

import com.example.fbtesting.models.Dish

interface ILocalDataSource {
    suspend fun insertMenuData(dish: Dish)

    suspend fun getMenuData(): List<Dish>
}