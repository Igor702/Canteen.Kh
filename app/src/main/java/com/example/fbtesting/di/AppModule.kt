package com.example.fbtesting.di

import android.content.Context
import androidx.room.Room
import com.example.fbtesting.model.local.MenuDao
import com.example.fbtesting.model.local.MenuDatabase
import dagger.Module
import dagger.Provides

@Module
class AppModule {

//here can be function for providing MenuDao if anything working


    @Provides
    fun getMenuDatabaseInstance(context: Context): MenuDatabase{
        val database: MenuDatabase = Room.databaseBuilder(context, MenuDatabase::class.java, "DISHES_DATABASE").build()
        return database

    }

    @Provides
    fun getMenuDao(database: MenuDatabase):MenuDao{
        return database.menuDao()
    }





    //todo: if next functions will working move it in separate class
    //get FirebaseDataLoader
//    @Singleton
//    @Provides
//    fun getFirebaseDataLoaderInstance():FirebaseDataLoader{
//        return FirebaseDataLoader()
//    }

    //get Repository instance
//    @Singleton
//    @Provides
//    fun getDataRepositoryInstance():DataRepository{
//        return DataRepository(getMenuDatabaseInstance(),getFirebaseDataLoaderInstance())
//    }
}