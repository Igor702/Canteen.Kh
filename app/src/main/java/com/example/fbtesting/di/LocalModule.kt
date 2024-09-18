package com.example.fbtesting.di

import android.content.Context
import androidx.room.Room
import com.example.fbtesting.data.local.LocalDataSource
import com.example.fbtesting.data.local.MenuDao
import com.example.fbtesting.data.local.MenuDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [LocalBindModule::class])
class LocalModule {
    @Provides
    fun getMenuDatabaseInstance(@ApplicationContext context: Context): MenuDatabase {
        return Room.databaseBuilder(
            context,
            MenuDatabase::class.java,
            "DISHES_DATABASE"
        ).build()

    }

    @Provides
    fun getMenuDao(database: MenuDatabase): MenuDao {
        return database.menuDao()
    }

    @Provides
    fun getLocalDataSource(dao: MenuDao): LocalDataSource {
        return LocalDataSource(dao)
    }
}