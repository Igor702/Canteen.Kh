package com.example.fbtesting.data

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.fbtesting.data.remote.EMAIL
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth

class FakeDataRepository:IDataRepository {

    private var data: List<Dish>? = null
    private var lastIndex:Int? = null
    private val sentOrders = mutableMapOf<String, Order>()
    override fun getUserEmail(): String? {
        return EMAIL
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