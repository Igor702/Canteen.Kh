package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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


    //don't do this
    override suspend fun getLastIndex(): Int {
        return FakeAndroidRepositoryHelper.getLastIndex()
    }

    override fun sendOrder(index: String, order: Order) {
        FakeAndroidRepositoryHelper.sendOrder(index, order)
    }

    override fun onOrderStatusChangedListener(): Flow<String> {
        return flow {  }
    }


}