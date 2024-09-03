package com.example.fbtesting.data.local

import com.example.fbtesting.models.Dish

class FakeLocalDataSource:ILocalDataSource {

    private val listOfDishesFake = mutableListOf<Dish>(
        Dish("1", "title1", "500", "url1"),
        Dish("1", "title2", "500", "url2"),
        Dish("1", "title3", "500", "url3")
    )
    override suspend fun insertMenuData(dish: Dish) {
        listOfDishesFake.add(dish)
    }


    override suspend fun getMenuData(): List<Dish> {
        return listOfDishesFake
    }
}