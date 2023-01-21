package com.example.fbtesting

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.fbtesting.di.AppComponent
import com.example.fbtesting.di.DaggerAppComponent
import com.example.fbtesting.model.remote.TAG
import javax.inject.Singleton

class MyApplication: Application() {

    init {
        Injector.init(this)
    }
}


object Injector{
    private lateinit var appComponent: AppComponent

    fun init(application: Application){
        appComponent = DaggerAppComponent.factory().create(application)

    }

    fun get(): AppComponent = appComponent

}


fun Fragment.getAppComponent(): AppComponent = Injector.get()
