package com.example.fbtesting.model

import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.model.remote.FirebaseDataLoader
import com.example.fbtesting.models.Dish
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
val TAG = "TAG"

class DataRepository(private val firebaseDataLoader: FirebaseDataLoader =
    FirebaseDataLoader().getLoader()
) {

    fun getRepository() = DataRepository()

    fun getAuth():FirebaseAuth{
        Log.d(TAG, "DataRepository, getAuth, auth: ${firebaseDataLoader.getFirebaseAuth()}")
        return firebaseDataLoader.getFirebaseAuth()
    }
    fun getOptions(): FirebaseRecyclerOptions<Dish> {
        return firebaseDataLoader.getOptions()
    }
    suspend fun getLastIndex():Int{
        val temp = firebaseDataLoader.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $temp")

        return temp

    }


    fun sendOrder(index: String, order: Order){
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        firebaseDataLoader.sendOrder(index, order)
    }
}