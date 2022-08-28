package com.example.fbtesting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fbtesting.databinding.FragmentExampleBinding
import com.example.fbtesting.models.Dish
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Comment
import java.util.HashMap

const val TAG = "TAG"

class ExampleFragment: Fragment() {

    val mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("User")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExampleBinding.inflate(inflater, container, false)

        val listOfUsers = mutableListOf<User>()

        val database = Firebase.database
        val myRef = database.getReference("message")
        var counter = 0
        binding.button.setOnClickListener {
            val id = ++counter
            lateinit var user: User
            binding.apply {
                val name = edName.text.toString()
                val surname = edSurname.text.toString()
                val mail = edEmail.text.toString()
                 user = User(id, name, surname, mail)
            }

            val dishes = mutableListOf<Dish>()

            for (i in 0..15){
                dishes.add(Dish(i.toString(), "title", "$i", "url"))
            }


            myRef.setValue(dishes)
            Toast.makeText(context, "Saved!!!", Toast.LENGTH_SHORT).show()
           Log.d("TAG", "user: $user")

//            val storageRef = storage.getReferenceFromUrl("gs://fir-testing-6b32d.appspot.com/images/teahub.io-mazda-rx7-wallpaper-hd-3286751.jpg")
//            Log.d("TAG", "storageRef: $storageRef")



        }

            // Read from the database
//            val data  = object : ChildEventListener{
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d(TAG, "onChildAdded:" +snapshot.key!!)
//
//                    val user = snapshot.getValue<User>().toString()
//                    Log.d(TAG, "user: $user")
//
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d(TAG, "onChildChanged: ${snapshot.key}")
//                    val user = snapshot.getValue<Comment>()
////                    val commentKey = dataSnapshot.ke
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            }
//            myRef.addChildEventListener(data)

//
//            var data = mutableListOf<HashMap<*,*>>()
//            myRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
////                    for (postSnapshot in dataSnapshot.children) {
////                        data.add(postSnapshot.value as HashMap<*, *>)
////                        Log.d(TAG, "list:$data")
////                    }
//                    data = dataSnapshot.value as MutableList<HashMap<*, *>>
//                    Log.d(TAG, "list:$data")
//
//
//                        for(user in data){
//                            Log.d(TAG, "user: $user")
//                            listOfUsers.add(user.toUser())
//                        }
//
//                    Log.d(TAG, "listOfUsers:$listOfUsers")
//
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                    // ...
//                }
//            })
//
//
//
//
        return binding.root
    }
//
//    override fun onStart() {
//        super.onStart()
//
//    }


}