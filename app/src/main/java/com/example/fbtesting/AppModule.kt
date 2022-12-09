package com.example.fbtesting

import android.app.Application
import android.content.Context
import com.example.fbtesting.model.local.MenuDatabase
import com.example.fbtesting.model.remote.FirebaseDataLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

//here can be function for providing MenuDao if anything working


    @Provides
    fun getMenuDatabaseInstance(context: Context): MenuDatabase{
        return MenuDatabase.getDatabase(context)

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