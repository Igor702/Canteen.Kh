package com.example.fbtesting.model

import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.model.local.MenuDao
import com.example.fbtesting.model.remote.FirebaseDataLoader
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

const val TAG = "TAG"

class DataRepository @Inject constructor(
    private val menuDAO: MenuDao,
    private val firebaseDataLoader: FirebaseDataLoader

){

    fun getAuth():FirebaseAuth?{
        val auth = firebaseDataLoader.getFirebaseAuth()
        Log.d(TAG, "DataRepository, getAuth, auth.currentUser: ${auth?.currentUser}")
        return auth
    }


    suspend fun getDatabaseData():List<Dish>?{

        Log.d(TAG, "DataRepository, before call to db")
        val databaseData = menuDAO.getMenuData()
        Log.d(TAG, "DataRepository, after call to db, databaseData: $databaseData")


        if (databaseData.isNotEmpty()){
            Log.d(TAG, "getOptions, have data")
            return databaseData
        }else{
            Log.d(TAG, "DataRepository, before call to firebaseDataLoader.getOptions")
            val firebaseData = firebaseDataLoader.getOptions()
            Log.d(TAG, "DataRepository, after call, firebaseDataLoader.getOptions: $firebaseData")

            for (i in firebaseData){
                menuDAO.insertMenuData(i)
            }
            Log.d(TAG, "DataRepository, getOptions, data inserted in database")

            return firebaseData
        }
    }

    suspend fun getLastIndex():Int{
        val index = firebaseDataLoader.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $index")

        return index
    }

    fun sendOrder(index: String, order: Order){
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        firebaseDataLoader.sendOrder(index, order)
    }
}