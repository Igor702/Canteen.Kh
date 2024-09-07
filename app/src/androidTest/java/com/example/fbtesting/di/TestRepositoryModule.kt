package com.example.fbtesting.di

import com.example.fbtesting.data.DataRepository
import com.example.fbtesting.data.FakeAndroidDataRepository
import com.example.fbtesting.data.IDataRepository
import dagger.Binds
import dagger.Module

@Module
interface TestRepositoryModule {

    @Binds
    fun bindFakeAndroidDataRepositoryToIDataRepository(dataRepository: FakeAndroidDataRepository): IDataRepository


}