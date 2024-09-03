package com.example.fbtesting.data

import com.example.fbtesting.data.local.FakeLocalDataSource
import com.example.fbtesting.data.remote.FakeRemoteDataSource
import com.example.fbtesting.data.remote.email
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Test

class DataRepositoryTest{

    private lateinit var repository: DataRepository

    @Before
    fun setUp(){
        val remote = FakeRemoteDataSource()
        val local = FakeLocalDataSource()

        repository = DataRepository(local, remote)
    }


    @Test
    fun getAuth_returnAuth(){
        val result = repository.getAuth()

        assertThat(result, IsNot(null))
        assertThat(result?.currentUser, IsEqual(email))
    }




}