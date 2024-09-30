package com.example.fbtesting.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order

object FakeAndroidDataLoader {

    private var data: List<Dish>? = null
    private var lastIndex:Int? = null
    private val sentOrders = mutableMapOf<String, Order>()
    private var currentUserEmail = MutableLiveData<String?>()

    fun testSetUserEmail(currentUserEmail:String?){
        this.currentUserEmail.postValue(currentUserEmail)
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

    fun getCurrentUserEmail(): LiveData<String?> {
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
        currentUserEmail.postValue(null)
    }


}