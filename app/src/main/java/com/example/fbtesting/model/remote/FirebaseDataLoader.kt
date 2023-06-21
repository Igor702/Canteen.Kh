package com.example.fbtesting.model.remote

import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.example.fbtesting.models.toDish
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


const val TAG = "TAG"

class FirebaseDataLoader @Inject constructor() {

    private val database = Firebase.database
    private val menuItemsRef = database.getReference("message")
    private  val orderRef: DatabaseReference = database.getReference("orders")
    private  val lastItemRef: DatabaseReference = database.getReference("lastOrderIndex")



    fun getFirebaseAuth():FirebaseAuth?{
        Log.d(TAG, "FirebaseDataLoader, getLoader")
        return Firebase.auth
    }

    suspend fun getOptions(): List<Dish>{
        Log.d(TAG, "FirebaseDataLoader, getOptions, before menuItemsRef.get()")

        var temp: List<MutableMap<String,String>> = mutableListOf()

        try {
            Log.d(TAG, "FirebaseDataLoader, getOptions, try, before call to server")
            temp = menuItemsRef.get().await().value as List<MutableMap<String, String>>
            Log.d(TAG, "FirebaseDataLoader, getOptions, try, temp is: $temp")

        }catch (e:Exception){
            Log.d(TAG, "FirebaseDataLoader, getOptions, exception is: $e")
        }
        Log.d(TAG, "FirebaseDataLoader, getOptions, after menuItemsRef.get(), temp is: $temp")
        val list: MutableList<Dish> = mutableListOf()

        @Suppress("UNCHECKED_CAST")
         for (i in temp){
             Log.d(TAG, "FirebaseDataLoader, getOptions, i: $i")
             list.add(i.toDish())
         }

        Log.d(TAG, "FirebaseDataLoader, getOptions, list is: $list")

        return list

    }

    fun sendOrder(index: String, order: Order){
        Log.d(com.example.fbtesting.model.TAG, "FirebaseDataLoader, sendOrder, index: $index, order: $order")
        orderRef.child(index).setValue(order)

        lastItemRef.setValue(index)
    }

   suspend fun getIndex():Int{
       Log.d(TAG, "FirebaseDataLoader, getIndex, temp is: deleted")
       return lastItemRef.get().await().value.toString().toInt()
    }
}