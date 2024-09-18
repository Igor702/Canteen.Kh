package com.example.fbtesting.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fbtesting.data_models.Dish

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuData(dish: Dish)


    @Query("SELECT * FROM DISHES_DATABASE")
    suspend fun getMenuData(): List<Dish>

}