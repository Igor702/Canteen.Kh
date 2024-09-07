package com.example.fbtesting.di

import android.content.Context
import com.example.fbtesting.data.DataRepository
import com.example.fbtesting.view_model.SharedViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalModule::class, RemoteBindModule::class, LocalBindModule::class, RepositoryBindModule::class])
interface AppComponent {

//    fun getRepo():DataRepository

    fun viewModelsFactory(): SharedViewModel.Factory


    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
}