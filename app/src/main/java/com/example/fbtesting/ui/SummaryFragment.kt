package com.example.fbtesting.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.data.TAG
import com.example.fbtesting.databinding.FragmentSummaryBinding
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {


//    companion object {
//        var order = mutableMapOf<String, Order>()
//        var keys = mutableListOf<String>()
//
//        fun fillDishes(): MutableMap<String, Int> {
//            val newMap = mutableMapOf<String, Int>()
//            val data = MenuDatabaseAdapter.dishes
//            for (i in data) {
//                newMap.put(i!!.title, 1)
//                keys.add(i.title)
//
//            }
//            return newMap
//
//        }
//
//    }


//    private val viewModel: SharedViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "SummaryFragment onAttach")
//        viewModel.getLastIndex()

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSummaryBinding.inflate(inflater, container, false)

        val navEntry =   findNavController().getBackStackEntry(R.id.menuFragment)
        val navigateToStatusFragment = {findNavController().navigate(R.id.action_summaryFragment_to_statusFragment)}
        binding.summaryComposeView.setContent {
            val viewModel = hiltViewModel<SharedViewModel>(navEntry)

            MaterialTheme {
                Surface {
                    OrdersSummaryScreen( viewModel = viewModel,context =this.requireContext() ,navigateToStatusFragment = navigateToStatusFragment)

                }
            }
        }

//
//        val adapter: SummaryAdapter = SummaryAdapter {
//
//            Log.d(TAG, "working, it:$it")
//
//            var temp = binding.totalPrice.text.toString().toInt()
//            Log.d(TAG, "Summary, working, temp is:$temp")
//            if (it > 0) {
//                temp += it
//
//            } else {
//                if (temp > 0) {
//                    temp += it
//                }
//            }
//
//            Log.d(TAG, "Summary, working, temp after is:$temp")
//
//            binding.totalPrice.text = temp.toString()
//
//        }
//
//
//
//
//        binding.recyclerViewOrder.adapter = adapter
//        val temp = viewModel.getChosenDishes()
//        Log.d("TAG", "submitList, list:$temp")
//
//        adapter.submitList(temp.toList())
//
//        Log.d("TAG", "submitList, list:${adapter.currentList}")
//
//
//
//        val currentUserEmail = viewModel.currentUserEmail.value
//
//        var paymentMethod = ""
//        binding.payByCard.setOnClickListener {
//            paymentMethod = "card"
//        }
//        binding.payByCash.setOnClickListener {
//            paymentMethod = "cash"
//        }
//
//
//        binding.btnSendOrder.setOnClickListener {
//
//            if (binding.totalPrice.text.toString().toInt() == 0) {
//                Toast.makeText(
//                    context,
//                    "Your order is empty! Lets add some food!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                if (paymentMethod != "") {
//
//                    var lastIndex: Int? = null
//
//                    viewModel.lastIndex.observe(this.viewLifecycleOwner) {
//                        Log.d(TAG, "SummaryFragment, lastIndex.observe: $it")
//
//                        lastIndex = it
//
//                    }
//
//                    Log.d(TAG, "SummaryFragment, lastIndex before modifying: $lastIndex")
//
//                    val newIndex: String = lastIndex?.plus(1).toString()
//
//                    val dishes = checkDishesCountAndRemoveZero(SummaryAdapter.dishes)
//
//                    //currentUserEmail won't be null, because this step available only for logged users
//                    val newOrder = Order(
//                        dishes,
//                        currentUserEmail!!,
//                        "false",
//                        binding.totalPrice.text.toString(),
//                        paymentMethod
//                    )
//
//                    Log.d(TAG, "SummaryFragment, tempIndex before sending order is: $newIndex")
//
//                    //check that order is valid before sending
//                    if (viewModel.sendOrder(newIndex, newOrder)) {
//
//                        order.put(newIndex, newOrder)
//                        Log.d(
//                            TAG,
//                            "SummaryFragment, setOnClick, tempOrder.totalPrise is: ${newOrder.totalPrice}"
//                        )
//                        findNavController().navigate(
//                            SummaryFragmentDirections.actionSummaryFragmentToStatusFragment(
//                                newIndex
//                            )
//                        )
//
//                    } else {
//                        Toast.makeText(context, "Something get wrong", Toast.LENGTH_SHORT).show()
//
//                    }
//                } else {
//                    Toast.makeText(context, "Choose payment method!", Toast.LENGTH_SHORT).show()
//                }
//
//
//            }
//
//            binding.cancelButton.setOnClickListener {
//                findNavController().navigate(R.id.action_summaryFragment_to_menuFragment)
//            }
//
//        }
        return binding.root


    }





    override fun onDestroy() {
        super.onDestroy()
//        MenuDatabaseAdapter.dishes = mutableListOf()
        Log.d(TAG, "onDestroy")

    }
}









