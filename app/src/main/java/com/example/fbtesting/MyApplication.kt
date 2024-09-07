package com.example.fbtesting

import android.app.Application
import com.example.fbtesting.di.AppComponent
import com.example.fbtesting.di.DaggerAppComponent

//open class MyApplication: Application() {
//
//    init {
//        Injector.init(this)
//    }
//}
//
//
//object Injector{
//    private lateinit var appComponent: AppComponent
//
//    fun init(application: Application){
//        appComponent = DaggerAppComponent.factory().create(application)
//
//    }
//
//    fun get(): AppComponent = appComponent
//
//}

open class MyApplication : Application() {

    open val daggerComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}


//fun Fragment.getAppComponent(): AppComponent = Injector.get()
