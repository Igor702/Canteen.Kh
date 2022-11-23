package com.example.fbtesting

import com.example.fbtesting.models.Dish

fun main() {
    println(toStr(listOf(Dish("1", "1", "1", "1"), Dish("2","2","2","2"))))

}

fun toStr(d:List<Dish>):String{
    return d.toString()

}

fun toList(s: String){

}