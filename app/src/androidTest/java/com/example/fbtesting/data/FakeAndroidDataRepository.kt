package com.example.fbtesting.data

import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FakeAndroidDataRepository @Inject constructor():IDataRepository {

    private var data: List<Dish>? = null
    private var lastIndex:Int? = null
    private val sentOrders = mutableMapOf<String, Order>()
    private var auth:FirebaseAuth? = null

    override fun getCurrentUserEmail(): FirebaseAuth? {
        return auth
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

    fun testSetAuth(auth: FirebaseAuth){
        this.auth = auth
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