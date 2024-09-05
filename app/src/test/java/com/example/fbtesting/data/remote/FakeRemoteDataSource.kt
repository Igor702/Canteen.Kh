package com.example.fbtesting.data.remote

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.internal.matchers.Or

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