package com.example.fbtesting.data.local

import com.example.fbtesting.data_models.Dish


class FakeAndroidLocalData : ILocalDataSource {
    private val listOfDishesFake = mutableListOf<Dish>(

    )

    fun setDishes(listOfDishes: List<Dish>) {
        listOfDishesFake.addAll(listOfDishes)
    }

    fun clearDishes() {
        listOfDishesFake.clear()
    }

    override suspend fun insertMenuData(dish: Dish) {
        listOfDishesFake.add(dish)
    }


    override suspend fun getMenuData(): List<Dish> {
        return listOfDishesFake
    }
}


