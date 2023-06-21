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
import com.example.fbtesting.R
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.convertOrderToString
import com.example.fbtesting.view_model.SharedViewModel
import com.example.fbtesting.databinding.FragmentSummaryBinding
import com.example.fbtesting.getAppComponent
import com.example.fbtesting.model.TAG
import com.example.fbtesting.ui.adapters.MenuDatabaseAdapter
import com.example.fbtesting.ui.adapters.SummaryAdapter

class SummaryFragment : Fragment() {


    companion object {
        var order = mutableMapOf<String, Order>()
        var keys = mutableListOf<String>()

        fun fillDishes(): MutableMap<String, Int> {
            var newMap = mutableMapOf<String, Int>()
            val data = MenuDatabaseAdapter.dishes
            for (i in data) {
                newMap.put(i!!.title, 1)
                keys.add(i!!.title)

            }
            return newMap

        }

    }




    private val viewModel: SharedViewModel by viewModels { getAppComponent().viewModelsFactory() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.setDishes(MenuDatabaseAdapter.dishes)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        var paymentMethod = ""
        binding.payByCard.setOnClickListener {
            paymentMethod = "card"
        }
        binding.payByCash.setOnClickListener {
            paymentMethod = "cash"
        }


        binding.btnSendOrder.setOnClickListener {

            if (binding.totalPrice.text.toString().toInt() == 0){
                Toast.makeText(context,"Your order is empty! Lets add some food!", Toast.LENGTH_SHORT).show()
            }else{
                if (paymentMethod != "") {
                    val lastIndex = viewModel.lastIndex.value

                    Log.d(TAG, "SummaryFragment, lastIndex before modifying: $lastIndex")

                    val temp: String = lastIndex?.plus(1).toString()

                   val dishes =  checkDishesCountAndRemoveZero(SummaryAdapter.dishes)


                    val newOrder = Order(
                        dishes,
                        auth?.currentUser?.email.toString(),
                        "false",
                        binding.totalPrice.text.toString(),
                        paymentMethod
                    )

                    Log.d(TAG, "SummaryFragment, tempIndex before sending order is: $temp")

                    viewModel.sendOrder(temp, newOrder)
                    order.put(temp, newOrder)

                    Log.d(TAG, "SummaryFragment, setOnClick, tempOrder.totalPrise is: ${newOrder.totalPrice}")
                    findNavController().navigate(
                        SummaryFragmentDirections.actionSummaryFragmentToStatusFragment(
                            temp
                        )
                    )

                } else {
                    Toast.makeText(context, "Choose payment method!!!", Toast.LENGTH_SHORT).show()
                }
            }


        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_summaryFragment_to_menuFragment)
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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sign_menu, menu)
        val signIn: MenuItem = menu.findItem(R.id.sign_in)
        val signOut: MenuItem = menu.findItem(R.id.sing_out)

            signIn.isVisible = false
            signOut.isVisible = true

        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.sing_out ->{
                //todo: code for sign out
                true
            }

            else ->  return super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        MenuDatabaseAdapter.dishes = mutableListOf()
        Log.d(TAG, "onDestroy")

    }
}




