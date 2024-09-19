package com.example.fbtesting.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fbtesting.data.FakeDataRepository
import com.example.fbtesting.data.remote.email
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.Dish
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SharedViewModelTest{

    private lateinit var repository: FakeDataRepository
    private lateinit var viewModel: SharedViewModel

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private  val listOfDishes = listOf(  Dish("1", "title1", "500", "url1"),
        Dish("1", "title2", "500", "url2"),
        Dish("1", "title3", "500", "url3")
    )



    @Before
    fun setUpViewModel() {
        repository = FakeDataRepository()

        viewModel = SharedViewModel(repository)
    }

    @Before
    fun setDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }


    @Test
    fun authValue_updatedFromRepository_equalToRepositoryAuth(){
        val repo = FakeDataRepository()
        val authResult = Mockito.mock(FirebaseAuth::class.java)
        repo.testSetAuth(authResult)
        val testViewModel = SharedViewModel(repo)
        val result = testViewModel.currentUserEmail.value
        assertThat(result, equalTo(authResult))

    }

    @Test
    fun loadMenuData_validData_setNewValueInMenuData(){
        repository.testSetData(listOfDishes)

        runTest {

            viewModel.loadMenuData()
        }

        val result = viewModel.menuData.getOrAwaitValue()
        assertThat(result, equalTo(listOfDishes))
    }

    @Test
    fun loadMenuData_nullData_resultNull(){
        runTest {

            viewModel.loadMenuData()
        }

        val result = viewModel.menuData.getOrAwaitValue()
        assertThat(result, equalTo(null))
    }

    @Test
    fun setDishes_setChosenDishesValue(){
        val mutableList = mutableListOf<Dish?>()
        mutableList.addAll(listOfDishes)
        viewModel.setDishes(mutableList)

        val result = viewModel.chosenDishes.getOrAwaitValue()

        assertThat(result, equalTo(mutableList))
    }

    @Test
    fun getLastIndex_setLastIndexValue(){
        repository.testSetIndex(12)

        runTest {
            viewModel.getLastIndex()

        }
        val result = viewModel.lastIndex.getOrAwaitValue()
        assertThat(result, equalTo(12))
    }

    @Test
    fun sendOrder_validData_sendNewOderReturnTrue(){
        val order = Order(mutableMapOf(Pair("Name", 14)), email, "false", "40", "cash")

        val result = viewModel.sendOrder("14", order)

        assertThat(result, equalTo(true))
        assertThat(repository.testGetOrders()["14"], equalTo(order))


    }

    @Test
    fun sendOrder_invalidData_notSendNewOderReturnFalse(){
        val order = Order(mutableMapOf(Pair("Name", 14)), email, "", "", "cash")

        val result = viewModel.sendOrder("14", order)

        assertThat(result, equalTo(false))
        assertThat(repository.testGetOrders().size, equalTo(0))
    }







}