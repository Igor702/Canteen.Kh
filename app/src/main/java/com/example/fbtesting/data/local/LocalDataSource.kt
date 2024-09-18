package com.example.fbtesting.data.local

import com.example.fbtesting.data_models.Dish

class LocalDataSource(private val menuDao: MenuDao) : ILocalDataSource {
    override suspend fun insertMenuData(dish: Dish) {
        menuDao.insertMenuData(dish)
    }

    override suspend fun getMenuData(): List<Dish> {
        return menuDao.getMenuData()
    }


}