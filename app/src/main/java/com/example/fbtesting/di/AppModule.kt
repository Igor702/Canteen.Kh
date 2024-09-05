package com.example.fbtesting.di

import android.content.Context
import androidx.room.Room
import com.example.fbtesting.data.DataRepository
import com.example.fbtesting.data.IDataRepository
import com.example.fbtesting.data.local.ILocalDataSource
import com.example.fbtesting.data.local.LocalDataSource
import com.example.fbtesting.data.local.MenuDao
import com.example.fbtesting.data.local.MenuDatabase
import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [LocalModule::class, RemoteBindModule::class, RepositoryBindModule::class])
class AppModule


@Module(includes = [LocalBindModule::class])
class LocalModule{
    @Provides
    fun getMenuDatabaseInstance(context: Context): MenuDatabase {
        return Room.databaseBuilder(
            context,
            MenuDatabase::class.java,
            "DISHES_DATABASE"
        ).build()

    }

    @Provides
    fun getMenuDao(database: MenuDatabase):MenuDao{
        return database.menuDao()
    }
    @Provides
    fun getLocalDataSource(dao: MenuDao):LocalDataSource{
        return LocalDataSource(dao)
    }
}

@Module
interface LocalBindModule{

    @Binds
    fun bindLocalDataSourceToILocalDataSource(localDataSource: LocalDataSource):ILocalDataSource
}



@Module
interface RemoteBindModule{

    @Binds
    fun bindRemoteDataSourceToIRemoteDataSource(remoteDataSource: RemoteDataSource):IRemoteDataSource
}

@Module
interface RepositoryBindModule{
    @Binds
    fun bindDataRepositoryToIDataRepository(dataRepository: DataRepository):IDataRepository
}

