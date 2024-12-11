package com.example.fbtesting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "TestActivity onCreate")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "TestActivity onDestroy")
    }
}