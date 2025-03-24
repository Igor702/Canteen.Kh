package com.example.fbtesting

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.example.fbtesting.data_models.Dish
import org.junit.Assert.assertFalse
import java.util.Timer
import kotlin.concurrent.schedule

const val EMAIL_EXAMPLE = "user.name@gmail.com"
const val PASS_EXAMPLE = "myPass986"

private val list = mutableListOf(
    Dish(id = "1", title = "title1", price = "10"),
    Dish(id = "2", title = "title2", price = "20"),
    Dish(id = "3", title = "title3", price = "30"),
    Dish(id = "4", title = "title4", price = "40"),
    Dish(id = "5", title = "title5", price = "50"),
    Dish(id = "6", title = "title6", price = "60"),
    Dish(id = "7", title = "title7", price = "70"),
    Dish(id = "8", title = "title8", price = "80"),
    Dish(id = "9", title = "title9", price = "90"),

    )

fun assertFalseForAll(vararg lambdas: Boolean) {
    for (i in lambdas) {
        assertFalse(i)
    }
}

fun getMenuData():List<Dish>{
    return list
}

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}
fun ComposeContentTestRule.waitUntilTimeout(
    timeoutMillis: Long
) {
    AsyncTimer.start(timeoutMillis)
    this.waitUntil(
        condition = { AsyncTimer.expired },
        timeoutMillis = timeoutMillis + 1000
    )
}
