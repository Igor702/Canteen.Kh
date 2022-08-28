package com.example.fbtesting.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fbtesting.databinding.FragmentSummaryBinding
import com.example.fbtesting.ui.SummaryFragment.Companion.totalPrice

class SummaryFragment: Fragment() {
    companion object{
       private var totalPrice = 0
        fun setTotalPrice(value: Int){
            Log.d("TAG", "SummaryFragment value: $value")
            totalPrice+=value
            Log.d("TAG", "SummaryFragment total: $totalPrice")


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSummaryBinding.inflate(inflater, container, false)
        val data = MenuAdapter.dishes

        Log.d("TAG", "SummaryFragment total2: $totalPrice")
        val temp = totalPrice

        Log.d("TAG", "SummaryFragment temp: $temp")


        binding.totalPrice.text = temp.toString()
        val adapter = SummaryAdapter()
        binding.recyclerViewOrder.adapter = adapter
        Log.d("TAG", "submitList")
        adapter.submitList(data)




        return binding.root
    }
}