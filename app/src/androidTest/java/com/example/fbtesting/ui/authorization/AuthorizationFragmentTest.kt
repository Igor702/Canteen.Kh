package com.example.fbtesting.ui.authorization

import android.os.Bundle
import android.util.Log
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.base.Joiner.on
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.MainActivity
import com.example.fbtesting.R
import com.example.fbtesting.data.AndroidTestUtils
import com.example.fbtesting.data.AndroidTestUtils.fakePair
import com.example.fbtesting.data.FakeAndroidDataLoader
import com.example.fbtesting.data_models.ui.NavAuthLambdas
import com.example.fbtesting.launchFragmentInHiltContainer
import com.google.android.gms.auth.api.Auth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthorizationFragmentTest{

    //todo: fix these tests

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>().also {
        Log.d(TAG, "Test set composeTestRule")

    }

    private val spy = Mockito.spy(fakePair)
    private val stringSignIn = "Sign In"

    private val stringSignUp = "Sign up"


    @Before
    fun setUp(){
        Log.d(TAG, "Before setUp")

        hiltRule.inject()
        Log.d(TAG, "Test after hiltRule.inject()")
//        composeTestRule.activityRule.scenario.onActivity {
//            Log.d(TAG, "composeTestRule findNavController")
//            findNavController(it, R.id.nav_host_fragment)
//        }
      val view =   composeTestRule.activity.findViewById<ComposeView>(R.id.authorization_compose_view)

        if (view != null){
            view.setContent {
                Log.d(TAG, "composeTestRule setContent")

                //mock fakePairs
                AuthorizationScreen(pair = fakePair)
            }
        }else{
            Log.d(TAG, "composeTestRule composeView is null")

        }

    }

    @After
    fun resetData(){
        Log.d(TAG, "After resetData")
        FakeAndroidDataLoader.resetData()
    }

    @Test
    fun userEmailNull_notNavigate_allAppropriateUIElementsAreVisible(){





        composeTestRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText(stringSignIn).assertIsDisplayed()
        composeTestRule.onNodeWithText(stringSignUp).assertIsDisplayed()



    }

    //this test use prod navigate instead of spy, but why?
    @Test
    fun userEmailValidData_navigateToMenuFragment(){


        Log.d(TAG, "Test verify(spy).navigateToMenu()")
        verify(spy).navigateToMenu()

    }


    @Test
    fun pressSignInButton_navigateToSignInFragment(){
//        val navController = mock (NavController::class.java)
//
//
//        launchFragmentInHiltContainer<AuthorizationFragment>{
//                    Navigation.setViewNavController(this.requireView(), navController)
//        }

//        onView(withId(R.id.btn_sign_up_auth)).perform(click())
//
//        verify(navController).navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToSignUpFragment())


        composeTestRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()


        composeTestRule.onNodeWithText(stringSignIn).performClick()

        verify(spy).navigateToSignIn()







    }


    @Test
    fun pressSignUpButton_navigateToSignUpFragment() {


        composeTestRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()

        composeTestRule.onNodeWithText(stringSignUp).performClick()

        verify(spy).navigateToSignUp()
    }


    }