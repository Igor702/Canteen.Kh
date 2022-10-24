package com.example.fbtesting.model.local

object RoomDataInjector {

    var appDatabase: MenuDatabase? = null

    fun injectDb(): MenuDatabase? {
        return appDatabase
    }
}