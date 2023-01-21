package com.example.fbtesting.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fbtesting.models.Dish


@Database(entities = [Dish::class], version = 1, exportSchema = false)
abstract class MenuDatabase: RoomDatabase() {

    abstract fun menuDao(): MenuDao

}