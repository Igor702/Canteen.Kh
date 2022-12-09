package com.example.fbtesting.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fbtesting.data_models.MyObjectForRoom


@Database(entities = [MyObjectForRoom::class], version = 1, exportSchema = false)
abstract class MenuDatabase: RoomDatabase() {

    abstract fun menuDao(): MenuDAO



    companion object{
        @Volatile
    private var INSTANCE: MenuDatabase? = null

        //here must be context as a parameter
        fun getDatabase(context: Context):MenuDatabase{
            return INSTANCE?: synchronized(this){
                val instance =Room.databaseBuilder(
                    context.applicationContext,
                    MenuDatabase::class.java,
                    "dishes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }

        }
    }
}