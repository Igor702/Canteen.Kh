package com.example.fbtesting.data_models

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fbtesting.data.remote.TAG


@Entity(tableName = "dishes_database")
data class Dish(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var price: String = "",
    var url: String = "",
    @ColumnInfo(name = "checked", defaultValue = "false") var checked: Boolean = false

)

fun MutableMap<String, String>.toDish(): Dish {
    Log.d(TAG, "toDish method, this: $this")

    val id = this.get("id").toString()
    val title = this.get("title").toString()
    val price = this.get("price").toString()
    val url = this.get("url").toString()
    val checked = this.get("checked").toBoolean()
    val temp =
        Dish(id, title, price, url, checked)
    //Dish(url,id, title, price)
    Log.d(TAG, "toDish method, dish: $temp")
    return temp


}