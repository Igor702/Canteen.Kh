package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data.local.ILocalDataSource
import com.example.fbtesting.data.local.LocalDataSource
import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data.remote.RemoteDataSource
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

const val TAG = "TAG"

class DataRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource

):IDataRepository{

    override fun getAuth():FirebaseAuth?{
        val auth = remoteDataSource.getFirebaseAuth()
        Log.d(TAG, "DataRepository, getAuth, auth.currentUser: ${auth?.currentUser}")
        return auth
    }


    override suspend fun getData():List<Dish>?{

        Log.d(TAG, "DataRepository, before call to db")
        val databaseData = localDataSource.getMenuData()
        Log.d(TAG, "DataRepository, after call to db, databaseData: $databaseData")


        if (databaseData.isNotEmpty()){
            Log.d(TAG, "getOptions, have data")
            return databaseData
        }else{
            Log.d(TAG, "DataRepository, before call to firebaseDataLoader.getOptions")
            val firebaseData = remoteDataSource.getMenuData()
            Log.d(TAG, "DataRepository, after call, firebaseDataLoader.getOptions: $firebaseData")

            for (i in firebaseData){
                localDataSource.insertMenuData(i)
            }
            Log.d(TAG, "DataRepository, getOptions, data inserted in database")

            return firebaseData
        }
    }

    override suspend fun getLastIndex():Int{
        val index = remoteDataSource.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $index")

        return index
    }

    override fun sendOrder(index: String, order: Order){
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        remoteDataSource.sendOrder(index, order)
    }
}