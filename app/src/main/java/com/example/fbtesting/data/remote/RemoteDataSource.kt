package com.example.fbtesting.data.remote

import android.util.Log
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.toDish
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


const val TAG = "TAG"

class RemoteDataSource @Inject constructor() : IRemoteDataSource {

    private val database = Firebase.database
    private val menuItemsRef = database.getReference("message")
    private val orderRef: DatabaseReference = database.getReference("orders")
    private val lastItemRef: DatabaseReference = database.getReference("lastOrderIndex")


    override fun getCurrentUserEmail(): String? {
        val data: String? = Firebase.auth.currentUser?.email
        Log.d(TAG, "FirebaseDataLoader, getCurrentUserEmail: ${data}")

        return data
    }

    override suspend fun getMenuData(): List<Dish> {

        return getListOfMenuData(getRawMenuData())

    }


    override fun sendOrder(index: String, order: Order) {
        Log.d(
            com.example.fbtesting.data.TAG,
            "FirebaseDataLoader, sendOrder, index: $index, order: $order"
        )
        orderRef.child(index).setValue(order)

        lastItemRef.setValue(index)
    }

    override suspend fun getIndex(): Int {
        Log.d(TAG, "FirebaseDataLoader, getIndex, temp is: deleted")
        return lastItemRef.get().await().value.toString().toInt()
    }

    private suspend fun getRawMenuData(): List<MutableMap<String, String>> {
        var temp: List<MutableMap<String, String>> = mutableListOf()

        try {
            Log.d(TAG, "FirebaseDataLoader, getOptions, try, before call to server")
            temp = menuItemsRef.get().await().value as List<MutableMap<String, String>>
            Log.d(TAG, "FirebaseDataLoader, getOptions, try, temp is: $temp")

        } catch (e: Exception) {
            Log.d(TAG, "FirebaseDataLoader, getOptions, exception is: $e")
        }

        return temp
    }

    private fun getListOfMenuData(rawData: List<MutableMap<String, String>>): MutableList<Dish> {
        val list: MutableList<Dish> = mutableListOf()

        @Suppress("UNCHECKED_CAST")
        for (i in rawData) {
            Log.d(TAG, "FirebaseDataLoader, getOptions, i: $i")
            list.add(i.toDish())
        }

        Log.d(TAG, "FirebaseDataLoader, getOptions, list is: $list")

        return list
    }
}