package com.example.fbtesting.models

import com.example.fbtesting.User
import java.util.HashMap

data class Dish(
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