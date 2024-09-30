package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.Dish
import com.google.firebase.auth.FirebaseAuth

class FakeDataRepository:IDataRepository {

    private var data: List<Dish>? = null
    private var lastIndex:Int? = null
    private val sentOrders = mutableMapOf<String, Order>()
    private var currentUserEmail:String? = null

    override fun getCurrentUserEmail(): String? {
        return currentUserEmail
    }

    override suspend fun getData(): List<Dish>? {
        return data
    }


    //don't do this
    override suspend fun getLastIndex(): Int {
        return lastIndex!!
    }

    override fun sendOrder(index: String, order: Order) {
        sentOrders[index] = order
    }

    fun testSetAuth(currentUserEmail:String?){
        Log.d(TAG, "testSetAuth, email:$currentUserEmail")
        this.currentUserEmail = currentUserEmail
    }

    fun testSetData(list:List<Dish>?){
        data = list
    }

    fun testSetIndex(index: Int){
        lastIndex = index
    }

    fun testGetOrders():MutableMap<String, Order>{
        return sentOrders
    }


}