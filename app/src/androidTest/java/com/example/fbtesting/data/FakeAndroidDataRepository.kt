package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FakeAndroidDataRepository @Inject constructor() : IDataRepository {


    override fun getCurrentUserEmail(): String? {
        val email = FakeAndroidRepositoryHelper.getCurrentUserEmail()
        Log.d(TAG, "FakeAndroidDataRepository, getUserEmail:$email")
        return email
    }

    override suspend fun getData(): List<Dish>? {
        val data = FakeAndroidRepositoryHelper.getData()
        Log.d(TAG, "FakeAndroidDataRepository, getData:$data")

        return data
    }


    override suspend fun getLastIndex(): Int {
        return FakeAndroidRepositoryHelper.getLastIndex()
    }

    override fun sendOrder(index: String, order: Order) {
        FakeAndroidRepositoryHelper.sendOrder(index, order)
    }

    override fun onOrderStatusChangedListener(): Flow<String> = callbackFlow {
        Log.d(TAG, "FakeAndroidDataRepository, onOrderStatusListener")


        val listener = object : TestListener {
            override fun onOrderStatusChanged(status: String) {
                Log.d(
                    TAG,
                    "FakeAndroidDataRepository, onOrderStatusListener, override, status:$status"
                )
                this@callbackFlow.trySendBlocking(status)


            }

        }

        FakeAndroidRepositoryHelper.setListener(listener)

        awaitClose {
            Log.d(TAG, "TestRepo awaitClose, remove listener")
            FakeAndroidRepositoryHelper.removeListener(listener)
        }
    }


}