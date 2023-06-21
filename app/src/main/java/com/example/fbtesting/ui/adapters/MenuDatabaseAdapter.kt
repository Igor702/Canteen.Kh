package com.example.fbtesting.ui.adapters

import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fbtesting.databinding.CardDishBinding
import com.example.fbtesting.model.TAG
import com.example.fbtesting.models.Dish
import com.example.fbtesting.ui.adapters.SummaryAdapter.Companion.dishes
import com.squareup.picasso.Picasso

class MenuDatabaseAdapter(var sortValue: Boolean) : ListAdapter<Dish, MenuDatabaseAdapter.ViewHolder>(DiffCallback) {





    companion object {

        var images: MutableList<ImageView> = mutableListOf()
        var dishes = mutableListOf<Dish?>()

    }

    object DiffCallback: DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
             return oldItem.price == newItem.price
        }

    }

    class ViewHolder(private var binding: CardDishBinding) :
        RecyclerView.ViewHolder(binding.root) {




        fun bind(item: Dish?, sorting: Boolean) {
//
            if (!sorting){
                binding.chbBought.apply {
                     isEnabled = false
                    binding.tvAddToBasket.text = "Log in to order"

                }
            }else{
                binding.chbBought.isEnabled = true
                binding.tvAddToBasket.text = "Add to basket"
            }
            fun toast(context:Context){
                Toast.makeText(context, "Sign in for ordering food!!!", Toast.LENGTH_LONG).show()

            }


                Log.d(TAG, "MenuDatabaseAdapter, sorting: $sorting ")



            Log.d("TAG", "MenuDatabaseAdapter, bind, item is: $item")
            binding.tvDishTitle.text = item?.title.toString()
            binding.tvPrice.text = item?.price.toString()

            val image = binding.ivDish
            Picasso.get().load(item?.url).into(image)
            images.add(image)
            Log.d("TAG", "images: $images")


            if (dishes.contains(item)){
                binding.chbBought.isChecked = true

                Log.d(TAG, "MenuDatabaseAdapter, bind, isChecked")
            }else{
                binding.chbBought.isChecked = false
                Log.d(TAG, "MenuDatabaseAdapter, bind, isNotChecked")

            }

            binding.chbBought.setOnClickListener {
                Log.d("TAG", "Checked dish, allDishesBeforeModify:${dishes}")

                Log.d("TAG", "Checked dish, click")


                if (binding.chbBought.isChecked) {
                    dishes.add(item)
                    Log.d(TAG, "MenuDatabaseAdapter, bind, setOnClickListener, dishes.add(), dishes: $dishes, item:$item")
                } else {
                    dishes.remove(item)
                    Log.d(TAG, "MenuDatabaseAdapter, bind, setOnClickListener, dishes.remove(), dishes: $dishes, item:$item")

                }
                Log.d("TAG", "Dishes: $dishes")
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapter = LayoutInflater.from(parent.context)

        return ViewHolder(
            CardDishBinding.inflate(adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = getItem(position), sortValue)
    }
}