package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data.local.ILocalDataSource
import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

const val TAG = "TAG"

class DataRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource

):IDataRepository{

    override fun getAuth():FirebaseAuth?{
        val auth = remoteDataSource.getFirebaseAuth()
        return auth
    }


    override suspend fun getData(): List<Dish> {

        val databaseData = localDataSource.getMenuData()


        if (databaseData.isNotEmpty()){
            return databaseData
        }else{
            val firebaseData = remoteDataSource.getMenuData()

            for (i in firebaseData){
                localDataSource.insertMenuData(i)
            }

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