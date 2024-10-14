package com.example.fbtesting.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fbtesting.data_models.Dish


@Database(entities = [Dish::class], version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)],  exportSchema = true)
abstract class MenuDatabase : RoomDatabase() {

    abstract fun menuDao(): MenuDao

}