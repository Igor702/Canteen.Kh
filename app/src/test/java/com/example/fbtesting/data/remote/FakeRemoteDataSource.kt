package com.example.fbtesting.data.remote

import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.Dish
import com.google.firebase.auth.FirebaseAuth

const val email = "ivan.zolo@gmail.com"
const val pass = "nekoglay"

class FakeRemoteDataSource:IRemoteDataSource {



    private val sentOrders = mutableMapOf<String, Order>()
    private var listOfDishesFake:MutableList<Dish>? = null
    private var auth:FirebaseAuth? = null


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

    fun testSetAuth(auth: FirebaseAuth){
        this.auth = auth
    }

    override fun getFirebaseAuth(): FirebaseAuth? {
        return auth
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