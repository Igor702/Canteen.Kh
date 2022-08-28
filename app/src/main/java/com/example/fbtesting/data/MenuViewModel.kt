package com.example.fbtesting.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fbtesting.TAG
import com.example.fbtesting.models.Dish
import com.example.fbtesting.models.toDish
import com.example.fbtesting.toUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class MenuViewModel: ViewModel() {

  private  val database = Firebase.database
  private  val myRef = database.getReference("message")
    var listOfUsers = mutableListOf<Dish>()

    init {
        getDataFromFirebase()
    }


    fun getDataFromFirebase():MutableList<Dish>{

        var temp: MutableList<Dish> = mutableListOf()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (postSnapshot in dataSnapshot.children) {
//                        data.add(postSnapshot.value as HashMap<*, *>)
//                        Log.d(TAG, "list:$data")
//                    }
                val data = dataSnapshot.value as MutableList<HashMap<*, *>>
                Log.d(TAG, "viewModel list:$data")


                for(user in data){
                    Log.d(TAG, "viewModel user: $user")
                    listOfUsers.add(user.toDish())
                }

                Log.d(TAG, "viewModel listOfUsers:$listOfUsers")
                 temp = listOfUsers

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

        return temp
    }
    }






