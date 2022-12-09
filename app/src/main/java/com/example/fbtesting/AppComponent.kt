package com.example.fbtesting

import android.content.Context
import com.example.fbtesting.model.DataRepository
import com.example.fbtesting.view_model.SharedViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
interface AppComponent {

    fun getRepo():DataRepository

    fun viewModelsFactory(): SharedViewModel.Factory



    //here can be shit, maybe it better to put mainActivity or something
//    fun inject(viewModel: SharedViewModel)
//    fun injectRepo(repository: DataRepository)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context):AppComponent
//        fun setApplication(@BindsInstance app: Application):ApplicationComponent
    }
}