package com.example.fbtesting.model.remote

import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.example.fbtesting.models.toDish
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.invoke.MethodHandles.Lookup
import javax.inject.Inject


val TAG = "TAG"

class FirebaseDataLoader @Inject constructor() {

    private val database = Firebase.database
    private val myRef = database.getReference("message")
    private  val orderRef: DatabaseReference = database.getReference("orders")
    private  val lastItemRef: DatabaseReference = database.getReference("lastOrderIndex")


    fun getLoader() = FirebaseDataLoader()

    fun getFirebaseAuth():FirebaseAuth{
        Log.d(TAG, "FirebaseDataLoader, getLoader")
        return Firebase.auth
    }
//    fun getOptions(): FirebaseRecyclerOptions<Dish> {
//        val temp  = FirebaseRecyclerOptions.Builder<Dish>()
//            .setQuery(myRef, Dish::class.java)
//            .build()
//        Log.d(TAG, "FirebaseDataLoader, getOptions ${temp.snapshots}")
//
//         lastItemRef.get().await().value.toString().toInt()
//
//        return temp
//    }

    suspend fun getOptions(): List<Dish>{
        val temp = myRef.get().await().value as List<MutableMap<String, String>>
        val list: MutableList<Dish> = mutableListOf()

        @Suppress("UNCHECKED_CAST")
         for (i in temp){
             Log.d(TAG, "FirebaseDataLoader, getOptions, i: $i")
             list.add(i.toDish())
//             Log.d(TAG, "FirebaseDataLoader, getOptions, i.toDish(): ${i.toDish()}")

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
//        try {
//            runBlocking {
//                val temp = lastItemRef.get()
//                Log.d(TAG, "FirebaseDataLoader, getIndex, try, temp: ${temp.result}")
//
//            }
//
//        }catch (e: Exception){
//            Log.d(TAG, "FirebaseDataLoader, getIndex, exception: $e")
//        }



//       lastItemRef.get().addOnSuccessListener {
//           Log.d(TAG,"FirebaseDataLoader, index, got value: ${it.value}")
//       }.addOnFailureListener {
//           Log.d(TAG,"FirebaseDataLoader, index, exception: $it")
//
//       }
//       if (temp.isComplete){
//           val temp1 = temp.toString()
//           Log.d(TAG, "FirebaseDataLoader, getIndex, temp1 String: $temp1")
//           val temp2 = temp1.toInt()
//           Log.d(TAG, "FirebaseDataLoader, getIndex, temp2 String: $temp2")
//
//           return temp2
//       }else{
//           Log.d(TAG, "FirebaseDataLoader, getIndex, shit: ${temp.isCanceled} ")
//
//       }

       Log.d(TAG, "FirebaseDataLoader, getIndex, temp is: deleted")
       return lastItemRef.get().await().value.toString().toInt()
    }
}