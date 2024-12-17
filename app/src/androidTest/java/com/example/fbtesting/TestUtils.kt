package com.example.fbtesting

import org.junit.Assert.assertFalse

const val EMAIL_EXAMPLE = "user.name@gmail.com"
const val PASS_EXAMPLE = "myPass986"

fun assertFalseForAll(vararg lambdas: Boolean) {
    for (i in lambdas) {
        assertFalse(i)
    }
}