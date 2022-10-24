package com.example.fbtesting.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentMenuBinding
import com.example.fbtesting.model.TAG
import com.example.fbtesting.ui.remote.MenuFirebaseAdapter
import com.example.fbtesting.view_model.SharedViewModel


class MenuFragment : Fragment() {


    private lateinit var adapter: MenuFirebaseAdapter
    val viewModel: SharedViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        Log.d("TAG", "MenuFragment")


        //todo: try catch block insted direct call to nullable options
        adapter = MenuFirebaseAdapter(viewModel.options.value!!)


        //todo: set adapter depend on cached data
        binding.recyclerViewMenu.adapter = adapter
        binding.btnToOrder.setOnClickListener {
            Log.d(TAG, "btnToOrder, ${MenuFirebaseAdapter.dishes}")

            if (MenuFirebaseAdapter.dishes.size != 0) {
                findNavController().navigate(R.id.action_menuFragment_to_summaryFragment)
            } else {
                Toast.makeText(context, "Choose some dish!!!", Toast.LENGTH_SHORT).show()

            }
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}


//    fun getDataFromFirebase(): MutableList<Dish> {
//
//        var temp: MutableList<Dish> = mutableListOf()
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
////                    for (postSnapshot in dataSnapshot.children) {
////                        data.add(postSnapshot.value as HashMap<*, *>)
////                        Log.d(TAG, "list:$data")
////                    }
//                val data = dataSnapshot.value as MutableList<HashMap<*, *>>
//                Log.d(TAG, "viewModel list:$data")
//
//
//                for (user in data) {
//                    Log.d(TAG, "viewModel user: $user")
//                    temp.add(user.toDish())
//                }
//
//                Log.d(TAG, "viewModel listOfUsers:$temp")
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                // ...
//            }
//        })
//
//        return temp
//
//    }
//}