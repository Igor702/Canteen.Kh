package com.example.fbtesting.data.remote

import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish

class
FakeAndroidRemoteData:IRemoteDataSource {
    private val sentOrders = mutableMapOf<String, Order>()
    private var listOfDishesFake:MutableList<Dish>? = null
    private var email:String? = null


    fun setDishes(listOfDishes: List<Dish>){
        listOfDishesFake = mutableListOf<Dish>()
        listOfDishesFake!!.addAll(listOfDishes)
    }

    fun clearDishes(){
        listOfDishesFake?.clear()
    }

    fun getOrders():MutableMap<String,Order>{
        return sentOrders
    }

    fun testSetUserEmail(email: String?){
        this.email = email
    }

    override fun getUserEmail(): String? {
        return email
    }


    override suspend fun getMenuData(): List<Dish> {
        return listOfDishesFake as List<Dish>
    }

    override fun sendOrder(index: String, order: Order) {
        sentOrders[index] = order
    }

    override suspend fun getIndex(): Int {
        return 15
    }

}
