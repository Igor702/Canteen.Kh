package com.example.fbtesting

import java.util.HashMap

data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
)

fun HashMap<*,*>.toUser():User{
    val id = this.get("id").toString().toInt()
    val name = this.get("name").toString()
    val surname = this.get("surname").toString()
    val email = this.get("email").toString()
    return User(id, name, surname, email)


}
