package com.example.fbtesting.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fbtesting.R
import com.example.fbtesting.data.FakeAndroidDataLoader
import com.example.fbtesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthorizationFragmentTest{

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @After
    fun resetData(){
        FakeAndroidDataLoader.resetData()
    }

    @Test
    fun userEmailNull_notNavigate_allAppropriateUIElementsAreVisible(){

        launchFragmentInHiltContainer<AuthorizationFragment>(Bundle(), R.style.Theme_FbTesting )

        onView(withId(R.id.btn_sign_in_auth))
            .check(matches(isDisplayed()))

    }

    @Test
    fun userEmailValidData_navigateToMenuFragment(){



        val navController = mock (NavController::class.java)



            launchFragmentInHiltContainer<AuthorizationFragment>{
                Navigation.setViewNavController(requireView(), navController)

            }

        FakeAndroidDataLoader.testSetUserEmail("example.user@gmail.com")






//        verify(navController).navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMenuFragment())
    }


    @Test
    fun pressSignUpButton_navigateToSignUpFragment(){
        val navController = mock (NavController::class.java)


        launchFragmentInHiltContainer<AuthorizationFragment>{
                    Navigation.setViewNavController(this.requireView(), navController)
        }

        onView(withId(R.id.btn_sign_up_auth)).perform(click())

        verify(navController).navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignUpFragment())

    }


}