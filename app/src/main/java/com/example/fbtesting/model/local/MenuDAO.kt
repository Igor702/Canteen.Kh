package com.example.fbtesting.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fbtesting.data_models.MyObjectForRoom

@Dao
interface MenuDAO {

    //todo: insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuData(roomDataObject: MyObjectForRoom)

    //todo: update


    //todo: get data
    @Query("SELECT * FROM DISHES_DATABASE")
    suspend fun getMenuData(): MyObjectForRoom

    fun getDao() = Dao()
}