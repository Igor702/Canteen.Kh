package com.example.fbtesting.ui.authorization

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.fbtesting.R
import com.example.fbtesting.data.IDataRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.fbtesting.TestMyApplication
import com.example.fbtesting.data.TestDataLoader
import org.junit.After
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest
@RunWith(AndroidJUnit4::class)
class AuthorizationFragmentTest{

    @Inject
    lateinit var repository: IDataRepository


    @Before
    fun setUpRepository(){
        val component = TestMyApplication().initializeComponent()

        component.inject(this)

    }
    @After
    fun resetData(){
        TestDataLoader.resetAll()
    }

    @Test
    fun userIsNotSigned_allElementsAreVisible(){
        val navController = mock(NavController::class.java)


      launchFragmentInContainer<AuthorizationFragment>(Bundle(), R.style.Theme_FbTesting)
          .onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.btn_sign_in_auth))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.sign_in)))

        onView(withId(R.id.btn_sign_up_auth))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.sign_up)))



    }

    @Test
    fun userIsSigned_navigateToMenuFragment(){
        TestDataLoader.testSetUserEmail("example.user@gmail.com")

        val navController = mock(NavController::class.java)


        launchFragmentInContainer<AuthorizationFragment>(Bundle(), R.style.Theme_FbTesting)
            .onFragment {
                Navigation.setViewNavController(it.view!!, navController)
            }

        verify(navController).navigate(R.id.action_authorizationFragment_to_menuFragment)




    }

}