package com.example.fbtesting.model.local

import android.util.Log
import com.example.fbtesting.model.TAG

object RoomDataInjector {

    private var appDatabase: MenuDatabase? = null

    fun injectDb(): MenuDatabase? {
        Log.d(TAG, "RoomDataInjector, injectDb")
        return appDatabase
    }
}