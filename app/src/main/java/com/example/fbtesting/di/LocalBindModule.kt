package com.example.fbtesting.di

import com.example.fbtesting.data.local.ILocalDataSource
import com.example.fbtesting.data.local.LocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface LocalBindModule {

    @Binds
    fun bindLocalDataSourceToILocalDataSource(localDataSource: LocalDataSource): ILocalDataSource
}