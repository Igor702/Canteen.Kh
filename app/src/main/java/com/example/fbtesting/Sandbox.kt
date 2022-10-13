package com.example.fbtesting

fun main() {
    val list = mutableListOf<String>()
    list.add("string 1")
    list.add("string 2")
    list.add("string 3")
    list.add("string 4")
    list.add("string 5")

    println(list.toString())

    var str: String = ""

    for (i in list){
        str+= "$i,\n"
    }


    print(str)
}