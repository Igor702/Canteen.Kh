package com.example.fbtesting.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fbtesting.data_models.MyObjectForRoom
import com.example.fbtesting.models.Dish

@Dao
interface MenuDAO {

    //todo: insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuData(dish: Dish)

    //todo: update


    //todo: get data
    @Query("SELECT * FROM DISHES_DATABASE")
    suspend fun getMenuData(): List<Dish>

//    fun getDao() = Dao()
}