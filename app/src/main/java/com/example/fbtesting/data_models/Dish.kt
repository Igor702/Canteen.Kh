package com.example.fbtesting.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.HashMap
@Entity(tableName = "dishes_database")
data class Dish(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val price: String = "",
    val url: String = ""

)

fun HashMap<*, *>.toDish(): Dish {
    val id = this.get("id").toString()
    val title = this.get("title").toString()
    val price = this.get("price").toString()
    val url = this.get("url").toString()
    return Dish(url,id, title, price)


}