package com.example.fbtesting.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.data.TAG
import com.example.fbtesting.databinding.FragmentMenuBinding
import com.example.fbtesting.ui.adapters.MenuDatabaseAdapter
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var isBackButtonWasPressed = false


    private lateinit var adapter: MenuDatabaseAdapter
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        Log.d("TAG", "MenuFragment")



        viewModel.loadMenuData()
        viewModel.menuData.observe(this.viewLifecycleOwner) {
            adapter = MenuDatabaseAdapter()
            Log.d(TAG, "MenuFragment, before submit list, temp: $it")

            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)

            } else {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show()
            }

            binding.recyclerViewMenu.adapter = adapter

        }





        binding.btnToOrder.setOnClickListener {
            Log.d(TAG, "btnToOrder, ${MenuDatabaseAdapter.dishes}")
            if (MenuDatabaseAdapter.dishes.size != 0) {
                isBackButtonWasPressed = false
                findNavController().navigate(R.id.action_menuFragment_to_summaryFragment)
            } else {
                Toast.makeText(context, "Choose some dish!!!", Toast.LENGTH_SHORT).show()

            }


        }


        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (isBackButtonWasPressed) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Press back button again for exit", Toast.LENGTH_SHORT)
                        .show()
                    isBackButtonWasPressed = true

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }


}



