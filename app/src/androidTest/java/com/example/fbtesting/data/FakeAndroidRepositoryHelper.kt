package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order

interface TestListener {
    fun onOrderStatusChanged(status: String)
}

object FakeAndroidRepositoryHelper {

    private var data: List<Dish>? = null
    private var lastIndex: Int? = null
    private val sentOrders = mutableMapOf<String, Order>()
    private var currentUserEmail: String? = EMAIL_EXAMPLE
    private var orderStatus = ORDER_COOKING

    private var listener: MutableList<TestListener> = mutableListOf()

    fun testSetUserEmail(testUserEmail: String?) {
        Log.d(TAG, "FakeAndroidDataLoader, setUserEmail:$testUserEmail")

        currentUserEmail = testUserEmail
        Log.d(TAG, "FakeAndroidDataLoader, setUserEmail, after set:$testUserEmail")


    }

    fun setListener(newListener: TestListener) {
        listener.add(newListener)
        Log.d(TAG, "FakeAndroidDataLoader, setListener: $listener, newListener:$newListener")

    }

    fun removeListener(removeListener: TestListener) {
        listener.remove(removeListener)
        Log.d(
            TAG,
            "FakeAndroidDataLoader, removeListener: $listener, removeListener:$removeListener"
        )

    }

    fun notifyChanges(newStatus: String) {
        listener.forEach {
            it.onOrderStatusChanged(newStatus)
            Log.d(TAG, "FakeAndroidDataLoader, notifyChanges: $listener, status:$newStatus")

        }
    }

    fun testSetData(list: List<Dish>?) {

        data = list
        Log.d(TAG, "FakeAndroidDataLoader, testSetData after set:$data")

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
        lastIndex = index.toInt()
    }

    fun resetData() {
        data = null
        lastIndex = null
        sentOrders.clear()
        currentUserEmail = EMAIL_EXAMPLE
        orderStatus = ORDER_COOKING
    }


}