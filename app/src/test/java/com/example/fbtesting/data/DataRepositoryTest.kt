package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.data.local.FakeLocalDataSource
import com.example.fbtesting.data.remote.FakeRemoteDataSource
import com.example.fbtesting.data.remote.email
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.example.fbtesting.view_model.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.notNull

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTest{

    private  val list = listOf(  Dish("1", "title1", "500", "url1"),
        Dish("1", "title2", "500", "url2"),
        Dish("1", "title3", "500", "url3")
    )

    private lateinit var repository: DataRepository
    private lateinit var remote:FakeRemoteDataSource
    private lateinit var local: FakeLocalDataSource

    @Before
    fun setUp(){
         remote = FakeRemoteDataSource()
        local = FakeLocalDataSource()

        repository = DataRepository(local, remote)
    }

    @After
    fun tearDown(){
        remote.clearDishes()
        local.clearDishes()
    }


    @Test
    fun getAuth_returnAuthEqualToRemoteDataStore(){
        val remoteData = FakeRemoteDataSource()
        val localData = FakeLocalDataSource()
        val authResult = Mockito.mock(FirebaseAuth::class.java)
        remoteData.testSetAuth(authResult)
        val repository = DataRepository(localData, remoteData)
        val result = repository.getAuth()
        assertThat(result, equalTo(authResult))
    }

    @Test
    fun sendOrder_validData_sendSuccess(){
        repository.sendOrder("14", Order(mutableMapOf(Pair("Name", 15)), email))
        val result = remote.getOrders()
        assertThat(result["14"]?.currentUser, equalTo(email))
    }

    @Test
    fun getData_dataStoredLocally_emptyRemote_returnLocalData() = runTest{
        local.setDishes(list)
        val result = repository.getData()
        assertThat(result?.size, IsEqual(3))
    }
    @Test
    fun getData_localDataIsEmpty_returnRemoteData()= runTest{
        remote.setDishes(list)
        val result = repository.getData()
        assertThat(result?.size, IsEqual(3))
    }

    @Test
    fun getLastIndex_returnIndex()= runTest{
        val result = repository.getLastIndex()
        assertThat(result, IsEqual(15))
    }








}