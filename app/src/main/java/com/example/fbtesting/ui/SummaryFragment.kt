package com.example.fbtesting.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.view_model.SharedViewModel
import com.example.fbtesting.databinding.FragmentSummaryBinding
import com.example.fbtesting.model.TAG
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SummaryFragment : Fragment() {


    companion object {
        var order = mutableMapOf<String, Order>()

        fun fillDishes(): MutableMap<String, Int> {
            var newMap = mutableMapOf<String, Int>()
            val data = MenuAdapter.dishes
            for (i in data) {
                newMap.put(i!!.title, 1)
            }
            return newMap

        }

    }

    private var lastIndex = 0

    private lateinit var database: FirebaseDatabase



    init {
        database = Firebase.database


    }

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.setDishes(MenuAdapter.dishes)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSummaryBinding.inflate(inflater, container, false)


        var adapter: SummaryAdapter? = SummaryAdapter {

            Log.d(TAG, "working, it:$it")

            var temp = binding.totalPrice.text.toString().toInt()
            Log.d(TAG, "Summary, working, temp is:$temp")
            if (it > 0) {
                temp += it

            } else {
                if (temp > 0) {
                    temp += it
                }
            }

            Log.d(TAG, "Summary, working, temp after is:$temp")

            binding.totalPrice.text = temp.toString()

        }

        binding.recyclerViewOrder.adapter = adapter
        Log.d("TAG", "submitList")
        adapter?.submitList(viewModel.chosenDishes.value)

        val auth = viewModel.auth.value

        var paymentMethod: String = ""
        binding.payByCard.setOnClickListener {
            paymentMethod = "card"
        }
        binding.payByCash.setOnClickListener {
            paymentMethod = "cash"
        }


        binding.btnSendOrder.setOnClickListener {

            if (paymentMethod != "") {
                val shit = viewModel.lastIndex.value

                Log.d(TAG, "SummaryFragment, lastIndex before modifying: $shit")

                val temp: String = shit?.plus(1).toString() //index++ working
                val tempOrder = Order(
                    SummaryAdapter.dishes,
                    auth?.currentUser?.email.toString(),
                    "false",
                    binding.totalPrice.text.toString(),
                    paymentMethod
                )

                Log.d(TAG, "SummaryFragment, tempIndex before sending order is: $temp")

                viewModel.sendOrder(temp, tempOrder)
                order.put(tempOrder.totalPrice, tempOrder)

                Log.d(
                    TAG,
                    "SummaryFragment, setOnClick, tempOrder.totalPrise is: ${tempOrder.totalPrice}"
                )
                findNavController().navigate(
                    SummaryFragmentDirections.actionSummaryFragmentToStatusFragment(
                        tempOrder.totalPrice
                    )
                )

                //todo: make sendBtn don't clickable after sending, while order status is not done

            } else {
                Toast.makeText(context, "Choose payment method!!!", Toast.LENGTH_SHORT).show()
            }
//            Log.d(TAG, "current user is: ${auth.currentUser?.email}")

        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_summaryFragment_to_menuFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        MenuAdapter.dishes = mutableListOf()
        Log.d(TAG, "onDestroy")

    }
}



//    fun createNotification(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
//                .apply {
//                    lightColor = Color.RED
//                    enableLights(true)
//                }
//            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        }
//    }


