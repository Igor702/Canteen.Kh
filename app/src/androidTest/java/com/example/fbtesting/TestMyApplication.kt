package com.example.fbtesting

import com.example.fbtesting.di.DaggerTestAppComponent
import com.example.fbtesting.di.TestAppComponent

class TestMyApplication:MyApplication() {
    override fun initializeComponent(): TestAppComponent {
        return DaggerTestAppComponent.create()
    }
}