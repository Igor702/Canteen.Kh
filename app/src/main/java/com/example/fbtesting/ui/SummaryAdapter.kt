package com.example.fbtesting.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fbtesting.databinding.CardOrderBinding
import com.example.fbtesting.models.Dish

import com.squareup.picasso.Picasso

class SummaryAdapter:  ListAdapter<Dish, SummaryAdapter.ViewHolder>(DiffCallback) {



    class ViewHolder(private var binding: CardOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Dish?) {
            Log.d("TAG", "adapter, bind, item is: $item")
            binding.tvDishTitle.text = item?.title.toString()
            binding.tvPrice.text = item?.price.toString()
            //            Picasso.get().load(item?.image).into(imageView)

            var image = binding.ivDish
                Picasso.get().load(item?.url).into(image)

            binding.btnPlus.setOnClickListener {
                Log.d("TAG", "summaryAdapter +")


                var temp = (item?.price!!.toInt())
                SummaryFragment.setTotalPrice(temp)


                Log.d("TAG", "TotalPrice:")
                val amount = binding.tvAmount.text.toString()
                var amountInt = 1

                Log.d("TAG", "summaryAdapter + amount:$amount")
                if(amount != ""){
                    amountInt =  amount.toInt()

                }
                Log.d("TAG", "summaryAdapter + amountInt:$amountInt")


                ++amountInt
                Log.d("TAG", "summaryAdapter + amountInt+1:$amountInt")


                binding.tvAmount.text = amountInt.toString()

            }
            binding.btnMinus.setOnClickListener {
                Log.d("TAG", "summaryAdapter -")

//                val temp = totalPrice
//                temp - item?.price!!.toInt()
//                if (temp>0){
//                    totalPrice - item.price.toInt()
//                    val amount = binding.tvAmount.text.toString()
//                    val amountInt =  amount.toInt()
//                    amountInt -1
//                    binding.tvAmount.text = amountInt.toString()
                }

            }



        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryAdapter.ViewHolder {
        val adapter = LayoutInflater.from(parent.context)

        return SummaryAdapter.ViewHolder(
            CardOrderBinding.inflate(adapter, parent, false)
        )
    }

    //    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(item = getItem(position))
//    }
//
    object DiffCallback : DiffUtil.ItemCallback<Dish>() {

        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.id == newItem.id
        }

    }
    override fun onBindViewHolder(holder: SummaryAdapter.ViewHolder, position: Int) {
        holder.bind(item = getItem(position))
    }
}






