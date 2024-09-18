package com.example.fbtesting.di

import com.example.fbtesting.data.DataRepository
import com.example.fbtesting.data.IDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryBindModule{
    @Binds
    fun bindDataRepositoryToIDataRepository(dataRepository: DataRepository): IDataRepository
}