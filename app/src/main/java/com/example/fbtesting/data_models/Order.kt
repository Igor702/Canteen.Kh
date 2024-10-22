package com.example.fbtesting.data_models


data class Order(
    val dishes: MutableMap<String, Int> = mutableMapOf(),
    val currentUser: String = "",
    var orderStatus: String = "",
    val totalPrice: String = "",
    val payBy: String = ""
)

fun HashMap<*, *>.toOrder(): Order {
    val dishes: MutableMap<String, Int> = this.get("dishes") as MutableMap<String, Int>
    val currentUser = this.get("currentUser").toString()
    val orderStatus = this.get("orderStatus").toString()
    val totalPrice = this.get("totalPrice").toString()
    val payBy = this.get("payBy").toString()

    return Order(dishes, currentUser, orderStatus, totalPrice, payBy)

}

fun MutableMap<String, Int>.convertOrderToString(): String {
    val temp = this.toList()
    var str = ""
    for (i in temp) {
        if (i == temp[temp.size - 1]) {
            str += "${i.first}: ${i.second}"

        } else {
            str += "${i.first}: ${i.second}\n"

        }
    }

    return str
}
