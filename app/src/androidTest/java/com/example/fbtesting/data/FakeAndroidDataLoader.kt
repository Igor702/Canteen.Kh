package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order

object FakeAndroidDataLoader {

    private var data: List<Dish>? = null
    private var lastIndex: Int? = null
    private val sentOrders = mutableMapOf<String, Order>()
    private var currentUserEmail: String? = null

    fun testSetUserEmail(testUserEmail: String?) {
        Log.d(TAG, "FakeAndroidDataLoader, setUserEmail:$testUserEmail")

        currentUserEmail = testUserEmail
        Log.d(TAG, "FakeAndroidDataLoader, setUserEmail, after set:$testUserEmail")


    }

    fun testSetData(list: List<Dish>?) {
        data = list
    }

    fun testSetIndex(index: Int) {
        lastIndex = index
    }

    fun testGetOrders(): MutableMap<String, Order> {
        return sentOrders
    }

    fun getCurrentUserEmail(): String? {
        Log.d(TAG, "FakeAndroidDataLoader, getUserEmail:$currentUserEmail")

        return currentUserEmail
    }

    fun getData(): List<Dish>? {
        return data
    }


    //don't do this
    fun getLastIndex(): Int {
        return lastIndex!!
    }

    fun sendOrder(index: String, order: Order) {
        sentOrders[index] = order
    }

    fun resetData() {
        data = null
        lastIndex = null
        sentOrders.clear()
        currentUserEmail = null
    }


}