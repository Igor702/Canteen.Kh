package com.example.fbtesting.data.local

import com.example.fbtesting.data_models.Dish

interface ILocalDataSource {
    suspend fun insertMenuData(dish: Dish)

    suspend fun getMenuData(): List<Dish>
}