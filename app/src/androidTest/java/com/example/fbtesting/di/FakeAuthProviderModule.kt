package com.example.fbtesting.di

import com.example.fbtesting.data.authorization.FakeAuthorizationProvider
import com.example.fbtesting.data.authorization.IAuthorizationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthProviderModule::class]
)
@Module
interface FakeAuthProviderModule {

    @Binds
    @JvmSuppressWildcards
    fun bindFakeAuthorizationProviderToIAuthorizationProvider(fakeAuthProvider: FakeAuthorizationProvider): IAuthorizationProvider


}