package com.example.fbtesting

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.fbtesting.model.remote.TAG
import javax.inject.Singleton

class MyApplication: Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//
//            appComponent = DaggerAppComponent.factory().create(this)
//    }
    init {
        Injector.init(this)
    }


}


object Injector{
    private lateinit var appComponent: AppComponent

    fun init(application: Application){
        appComponent = DaggerAppComponent.factory().create(application)
    }

    fun get():AppComponent = appComponent

}




//todo:here bullshit
//val Context.appComponent: AppComponent get() =  appComponent
//    when(this){
//
//    is MyApplication -> {
//        Log.d("TAG","Context.appComponent: $appComponent")
//
//        appComponent
//
//    }
//    else -> this.applicationContext.appComponent
//}

fun Fragment.getAppComponent(): AppComponent = Injector.get()
//{
//    Log.d(TAG, "Fragment.getAppComponent")
//   val temp = (this.requireActivity().application as MyApplication)
//    Log.d(TAG, "Fragment.getAppComponent, temp: $temp")
//
//    return temp
//}
