package com.example.fbtesting.di

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.fbtesting.TestMyApplication

class MyCustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestMyApplication::class.java.name, context)
    }
}