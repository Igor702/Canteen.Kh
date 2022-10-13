package com.example.fbtesting.ui

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fbtesting.TAG
import com.example.fbtesting.databinding.CardOrderBinding
import com.example.fbtesting.models.Dish

import com.squareup.picasso.Picasso



class SummaryAdapter(val itemClick: (Int)-> Unit):  ListAdapter<Dish, SummaryAdapter.ViewHolder>(DiffCallback) {
    companion object{
        val dishes = SummaryFragment.fillDishes()
    }


    class ViewHolder(private var binding: CardOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(item: Dish?, itemClick: (Int) -> Unit) {
            Log.d("TAG", "adapter, bind, item is: $item")
            binding.tvDishTitle.text = item?.title.toString()
            binding.tvPrice.text = item?.price.toString()
            itemClick(item?.price!!.toInt())
            var image = binding.ivDish
                Picasso.get().load(item.url).into(image)

            binding.btnPlus.setOnClickListener {
                Log.d("TAG", "summaryAdapter +")


                var temp = (item.price.toInt())
                itemClick(temp)

                Log.d("TAG", "TotalPrice:")
                val amount = binding.tvAmount.text.toString()
                var amountInt = 1

                Log.d("TAG", "summaryAdapter + amount:$amount")
                if(amount != ""){
                    amountInt =  amount.toInt()
                }
                Log.d("TAG", "summaryAdapter + amountInt:$amountInt")


                if (amountInt <15){
                    ++amountInt
                }
                Log.d("TAG", "summaryAdapter + amountInt+1:$amountInt")
                dishes.replace(item.title, amountInt)
                Log.d(TAG, "our map: $dishes")

                binding.tvAmount.text = amountInt.toString()

            }
            binding.btnMinus.setOnClickListener {
                Log.d("TAG", "summaryAdapter +")
                var temp = (item.price.toInt())
                itemClick(-temp)

                Log.d("TAG", "TotalPrice:")
                val amount = binding.tvAmount.text.toString()
                var amountInt = 1

                Log.d("TAG", "summaryAdapter + amount:$amount")
                if(amount != ""){
                    amountInt =  amount.toInt()

                }
                Log.d("TAG", "summaryAdapter + amountInt:$amountInt")


                if (amountInt != 0){
                    --amountInt

                }
                Log.d("TAG", "summaryAdapter + amountInt+1:$amountInt")

                dishes.replace(item.title, amountInt)
                binding.tvAmount.text = amountInt.toString()

            }

            }

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryAdapter.ViewHolder {
        val adapter = LayoutInflater.from(parent.context)

        return SummaryAdapter.ViewHolder(
            CardOrderBinding.inflate(adapter, parent, false),

        )
    }

    object DiffCallback : DiffUtil.ItemCallback<Dish>() {

        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.id == newItem.id
        }

    }
    override fun onBindViewHolder(holder: SummaryAdapter.ViewHolder, position: Int) {
        holder.bind(item = getItem(position), itemClick)
    }
}






