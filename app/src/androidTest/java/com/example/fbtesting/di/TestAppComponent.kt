package com.example.fbtesting.di

import com.example.fbtesting.ui.authorization.AuthorizationFragmentTest
import dagger.Component

@Component(modules = [TestRepositoryModule::class])
interface TestAppComponent:AppComponent {

    fun inject(test: AuthorizationFragmentTest)

}