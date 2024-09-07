package com.example.fbtesting.di

import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data.remote.RemoteDataSource
import dagger.Binds
import dagger.Module

@Module
interface RemoteBindModule{

    @Binds
    fun bindRemoteDataSourceToIRemoteDataSource(remoteDataSource: RemoteDataSource): IRemoteDataSource
}