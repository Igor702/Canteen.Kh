package com.example.fbtesting.di

import android.content.Context
import com.example.fbtesting.model.DataRepository
import com.example.fbtesting.view_model.SharedViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun getRepo():DataRepository

    fun viewModelsFactory(): SharedViewModel.Factory


    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
}