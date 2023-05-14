package com.example.fbtesting.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fbtesting.R
import com.example.fbtesting.databinding.FragmentMenuBinding
import com.example.fbtesting.getAppComponent
import com.example.fbtesting.model.TAG
import com.example.fbtesting.ui.adapters.MenuDatabaseAdapter
import com.example.fbtesting.view_model.SharedViewModel


class MenuFragment : Fragment() {


    private lateinit var adapter: MenuDatabaseAdapter
    private val viewModel: SharedViewModel by viewModels { getAppComponent().viewModelsFactory() }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        Log.d("TAG", "MenuFragment")


            viewModel.options.observe(this.viewLifecycleOwner) {
                adapter = MenuDatabaseAdapter()
                Log.d(TAG, "MenuFragment, before submit list, temp: $it")

                adapter.submitList(it)

                binding.recyclerViewMenu.adapter = adapter

            }

        binding.btnToOrder.setOnClickListener {
            Log.d(TAG, "btnToOrder, ${MenuDatabaseAdapter.dishes}")

            if (MenuDatabaseAdapter.dishes.size != 0) {
                findNavController().navigate(R.id.action_menuFragment_to_summaryFragment)
            } else {
                Toast.makeText(context, "Choose some dish!!!", Toast.LENGTH_SHORT).show()

            }
        }


        return binding.root
    }


}

