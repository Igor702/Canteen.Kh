package com.example.fbtesting.data_models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.fbtesting.model.TAG
import com.example.fbtesting.models.Dish
import com.example.fbtesting.models.toDish


@Entity(tableName = "dishes_database")
//@TypeConverters(MyConvertor::class)
data class MyObjectForRoom(
    @PrimaryKey
    var data: String = ""
//    val data: MutableList<Dish> = mutableListOf(),

    )




class MyConvertor(){
//    @TypeConverter
    fun fromList(d:List<Dish>?): String{
        Log.d(TAG, "Convertor, fromList")
        return d.toString()


    }
//    @TypeConverter
    fun stringToList(s: String): List<Dish>?{


        val temp2 =  s.split("(", ")")
        val list = mutableListOf<String>()
        for (i in temp2){
            if (i.length> 13){
                list.add(i)
            }
        }

        val listOfDishes = mutableListOf<Dish>()
        for (i in list){
            val listOfValues = mutableMapOf<String, String>()
            val temp =  i.split(", ")
            for (t in temp){
                val tempList = t.split("=")
                listOfValues.put(tempList[0], tempList[1])
            }

            listOfDishes.add(listOfValues.toDish())

        }
        Log.d(TAG, "Convertor, stringToList, listOfDishes: $listOfDishes")

        return listOfDishes

    }
}