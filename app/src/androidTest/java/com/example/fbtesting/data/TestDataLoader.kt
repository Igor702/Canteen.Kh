package com.example.fbtesting.data

import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish

object TestDataLoader {

    private var sentOrders:MutableMap<String, Order>? = mutableMapOf<String, Order>()
    private var listOfDishesFake:MutableList<Dish>? = null
    private var email:String? = null
    private var index:Int? = null


    fun setDishes(listOfDishes: List<Dish>){
        listOfDishesFake = mutableListOf<Dish>()
        listOfDishesFake!!.addAll(listOfDishes)
    }

    fun resetAll(){
        sentOrders = null
        listOfDishesFake = null
        email = null
        index = null
    }

    fun getOrders():MutableMap<String, Order>{
        return sentOrders!!
    }


    fun testSetUserEmail(email: String?){
        this.email = email
    }

    fun getUserEmail(): String? {
        return email
    }


    suspend fun getMenuData(): List<Dish> {
        return listOfDishesFake as List<Dish>
    }

    fun sendOrder(index: String, order: Order) {
        sentOrders?.set(index, order)
    }

    suspend fun getIndex(): Int {
        return index!!
    }
}