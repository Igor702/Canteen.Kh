package com.example.fbtesting.model

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.fbtesting.data_models.MyConvertor
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.MyObjectForRoom
import com.example.fbtesting.model.local.MenuDatabase
import com.example.fbtesting.model.local.RoomDataInjector
import com.example.fbtesting.model.remote.FirebaseDataLoader
import com.example.fbtesting.models.Dish
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Component
import javax.inject.Inject

val TAG = "TAG"

class DataRepository @Inject constructor(
//    private val firebaseDataLoader: FirebaseDataLoader =
//        FirebaseDataLoader().getLoader(),
//    private val appDatabase: MenuDatabase? = MenuDatabase.getDatabase()
val appDatabase: MenuDatabase,
val firebaseDataLoader: FirebaseDataLoader

)

//        RoomDataInjector.injectDb())
{



//    fun getRepository() = DataRepository()



    fun getAuth():FirebaseAuth{
        Log.d(TAG, "DataRepository, getAuth, auth: ${firebaseDataLoader.getFirebaseAuth()}")
        return firebaseDataLoader.getFirebaseAuth()
    }
//   suspend fun getOptions(): FirebaseRecyclerOptions<Dish> {
//       //todo: convert to string and change db entity class to string or something similar and use different recyclerViewAdapters
//        val temp =  firebaseDataLoader.getOptions()
//        appDatabase?.menuDao()?.insertMenuData(MyObjectForRoom(temp.snapshots.toList().map {
//            it as Dish
//        }))
//       Log.d(TAG, "DataRepository, getOptions, insertDataToDb, temp: ${temp.snapshots.toList()}")
//        return temp
//    }

    suspend fun getOptions():List<Dish>{
        val databaseData = appDatabase.menuDao().getMenuData()
        val temp = firebaseDataLoader.getOptions()
        Log.d(TAG, "DataRepository, getOptions, database is: ${databaseData.data}")


        if (databaseData.data == null){
            appDatabase.menuDao().insertMenuData(MyObjectForRoom(temp.toString()))
            Log.d(TAG, "DataRepository, getOptions, temp: $temp")

        }
        var temp1: MyObjectForRoom? = null
        try {
             temp1 = appDatabase?.menuDao()?.getMenuData()
            Log.d(TAG, "DataRepository, getOptions, temp: $temp1")

        }catch (e: Exception){
            Log.d(TAG, "DataRepository, getOptions, temp exception: $e")


        }

        Log.d(TAG, "DataRepository, getOptions, temp before returning: $temp1")
    //todo: here shit, temp1 returning null, but getOptions getting data to temp


        return temp
    }

    suspend fun getLocalData(): List<Dish>? {
        Log.d(TAG, "DataRepository, getLocalData")

        return MyConvertor().stringToList(appDatabase?.menuDao()?.getMenuData()?.data!!)

    }
    suspend fun getLastIndex():Int{
        val temp = firebaseDataLoader.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $temp")

        return temp

    }


    fun sendOrder(index: String, order: Order){
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        firebaseDataLoader.sendOrder(index, order)
    }
}