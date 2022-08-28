package com.example.fbtesting.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fbtesting.databinding.CardDishBinding
import com.example.fbtesting.models.Dish
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

class MenuAdapter(options: FirebaseRecyclerOptions<Dish>) :
    FirebaseRecyclerAdapter<Dish, MenuAdapter.ViewHolder>(options) {
    companion object{
        var images:MutableList<ImageView> = mutableListOf()
        var dishes = mutableListOf<Dish?>()

    }


    class ViewHolder(private var binding: CardDishBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Dish?) {
            Log.d("TAG", "adapter, bind, item is: $item")
            binding.tvDishTitle.text = item?.title.toString()
            binding.tvPrice.text = item?.price.toString()
            //            Picasso.get().load(item?.image).into(imageView)

            //TODO: add loaded images to list and use it
            var image = binding.ivDish
                Picasso.get().load(item?.url).into(image)
                images.add(image)
                Log.d("TAG", "images: $images")






            binding.chbBought.setOnClickListener {
                if (binding.chbBought.isChecked){
                    Log.d("TAG", "Checked dish, dishes:$dishes")
                    dishes.add(item)
                }else{
                    dishes.remove(item)
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

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(item = getItem(position))
//    }
//
//    companion object DiffCallback : DiffUtil.ItemCallback<Dish>() {
//
//        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Dish) {
        holder.bind(item = getItem(position))
    }
}