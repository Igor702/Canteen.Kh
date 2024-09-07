package com.example.fbtesting.data

import com.example.fbtesting.data.local.FakeAndroidLocalData
import com.example.fbtesting.data.remote.FakeAndroidRemoteData
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

const val EMAIL = "example.user@gmail.com"

class FakeAndroidDataRepository @Inject constructor():IDataRepository {
    override fun getUserEmail(): String? {
        return TestDataLoader.getUserEmail()
    }


    override suspend fun getData(): List<Dish>? {
        return TestDataLoader.getMenuData()
    }

    override suspend fun getLastIndex(): Int {
        return TestDataLoader.getIndex()
    }

    override fun sendOrder(index: String, order: Order) {
        TestDataLoader.sendOrder(index, order)
    }
}