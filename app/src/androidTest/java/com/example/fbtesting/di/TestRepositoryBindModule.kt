package com.example.fbtesting.di

import com.example.fbtesting.data.DataRepository
import com.example.fbtesting.data.FakeAndroidDataRepository
import com.example.fbtesting.data.IDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryBindModule::class]
)
@Module
interface TestRepositoryBindModule {

    @Binds
    fun bindFakeDataRepositoryToIDataRepository(fakeDataRepository: FakeAndroidDataRepository): IDataRepository
}