package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data.local.ILocalDataSource
import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import javax.inject.Inject

const val TAG = "TAG"

class DataRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource

) : IDataRepository {

    override fun getCurrentUserEmail(): String? {
        val mail = remoteDataSource.getCurrentUserEmail()
        return mail
    }


    override suspend fun getData(): List<Dish> {

        val databaseData = localDataSource.getMenuData()


        if (databaseData.isNotEmpty()) {
            Log.d(TAG, "DataRepository, databaseData,  $databaseData")

            return databaseData
        } else {
            val firebaseData = remoteDataSource.getMenuData()

            for (i in firebaseData) {
                localDataSource.insertMenuData(i)
            }

            Log.d(TAG, "DataRepository, firebaseData,  $firebaseData")

            return firebaseData
        }
    }

    override suspend fun getLastIndex(): Int {
        val index = remoteDataSource.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $index")

        return index
    }

    override fun sendOrder(index: String, order: Order) {
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        remoteDataSource.sendOrder(index, order)
    }
}