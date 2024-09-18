package com.example.fbtesting.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fbtesting.databinding.CardDishBinding
import com.example.fbtesting.data.TAG
import com.example.fbtesting.data_models.Dish

class MenuDatabaseAdapter() : ListAdapter<Dish, MenuDatabaseAdapter.ViewHolder>(DiffCallback) {





    companion object {

        var dishes = mutableListOf<Dish?>()

        //todo:change it to function call in constructor like in SummaryAdapter

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




        fun bind(item: Dish?) {





            Log.d("TAG", "MenuDatabaseAdapter, bind, item is: $item")
            binding.tvDishTitle.text = item?.title.toString()
            binding.tvPrice.text = item?.price.toString()

            val image = binding.ivDish
//            Picasso.get().load(item?.url).into(image)

            binding.ivDish.load(item?.url)


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
        holder.bind(item = getItem(position))
    }
}