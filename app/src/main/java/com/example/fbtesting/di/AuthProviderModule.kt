package com.example.fbtesting.di

import com.example.fbtesting.data.authorization.AuthorizationProvider
import com.example.fbtesting.data.authorization.IAuthorizationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
interface AuthProviderModule {
    @Binds
    fun bindAuthorizationProviderToIAuthorizationProvider(authorizationProvider: AuthorizationProvider): IAuthorizationProvider
}