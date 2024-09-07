package com.example.fbtesting.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.MyApplication
import com.example.fbtesting.R
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.view_model.SharedViewModel
import com.example.fbtesting.databinding.FragmentSummaryBinding
import com.example.fbtesting.data.TAG
import com.example.fbtesting.ui.adapters.MenuDatabaseAdapter
import com.example.fbtesting.ui.adapters.SummaryAdapter

class SummaryFragment : Fragment() {


    companion object {
        var order = mutableMapOf<String, Order>()
        var keys = mutableListOf<String>()

        fun fillDishes(): MutableMap<String, Int> {
            val newMap = mutableMapOf<String, Int>()
            val data = MenuDatabaseAdapter.dishes
            for (i in data) {
                newMap.put(i!!.title, 1)
                keys.add(i!!.title)

            }
            return newMap

        }

    }




    private val viewModel: SharedViewModel by viewModels {
        (this.requireActivity().application as MyApplication)
            .daggerComponent.viewModelsFactory() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.setDishes(MenuDatabaseAdapter.dishes)
        Log.d(TAG, "SummaryFragment onAttach")
        viewModel.getLastIndex()

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSummaryBinding.inflate(inflater, container, false)


        val adapter: SummaryAdapter = SummaryAdapter {

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
        adapter.submitList(viewModel.chosenDishes.value)


        val userEmail = viewModel.userEmail

        var paymentMethod = ""
        binding.payByCard.setOnClickListener {
            paymentMethod = "card"
        }
        binding.payByCash.setOnClickListener {
            paymentMethod = "cash"
        }


        binding.btnSendOrder.setOnClickListener {

            if (binding.totalPrice.text.toString().toInt() == 0) {
                Toast.makeText(
                    context,
                    "Your order is empty! Lets add some food!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (paymentMethod != "") {

                    var lastIndex: Int? = null

                    viewModel.lastIndex.observe(this.viewLifecycleOwner) {
                        Log.d(TAG, "SummaryFragment, lastIndex.observe: $it")

                        lastIndex = it

                    }

                    Log.d(TAG, "SummaryFragment, lastIndex before modifying: $lastIndex")

                    val newIndex: String = lastIndex?.plus(1).toString()

                    val dishes = checkDishesCountAndRemoveZero(SummaryAdapter.dishes)


                    val newOrder = Order(
                        dishes,
                        userEmail.value!!,
                        "false",
                        binding.totalPrice.text.toString(),
                        paymentMethod
                    )

                    Log.d(TAG, "SummaryFragment, tempIndex before sending order is: $newIndex")

                    //check that order is valid before sending
                    if (viewModel.sendOrder(newIndex, newOrder)) {

                        order.put(newIndex, newOrder)
                        Log.d(
                            TAG,
                            "SummaryFragment, setOnClick, tempOrder.totalPrise is: ${newOrder.totalPrice}"
                        )
                        findNavController().navigate(
                            SummaryFragmentDirections.actionSummaryFragmentToStatusFragment(
                                newIndex
                            )
                        )

                    } else {
                        Toast.makeText(context, "Something get wrong", Toast.LENGTH_SHORT).show()

                    }
                }else{
                    Toast.makeText(context, "Choose payment method!", Toast.LENGTH_SHORT).show()
                }


            }

            binding.cancelButton.setOnClickListener {
                findNavController().navigate(R.id.action_summaryFragment_to_menuFragment)
            }

        }
        return binding.root


    }

    private fun checkDishesCountAndRemoveZero(dishes: MutableMap<String, Int>):MutableMap<String,Int> {
       for (i in keys){
           if(dishes[i] == 0){
               dishes.remove(i)
           }
       }

        Log.d(TAG,"SummaryFragment, checkDishesCountAndRemoveZero, dishes $dishes")
        return dishes
    }



    override fun onDestroy() {
        super.onDestroy()
        MenuDatabaseAdapter.dishes = mutableListOf()
        Log.d(TAG, "onDestroy")

    }
    }









