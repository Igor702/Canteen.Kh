package com.example.fbtesting.ui

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.CanteenNavHost
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.HINT_ENTER_PASS
import com.example.fbtesting.PASS_EXAMPLE
import com.example.fbtesting.R
import com.example.fbtesting.ScreenAuthorization
import com.example.fbtesting.ScreenForgot
import com.example.fbtesting.ScreenMenu
import com.example.fbtesting.ScreenSignIn
import com.example.fbtesting.ScreenSignUp
import com.example.fbtesting.TAG
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.FakeAndroidRepositoryHelper
import com.example.fbtesting.data.FakeAndroidRepositoryHelper.getLastIndex
import com.example.fbtesting.data.authorization.FakeAuthorizationProviderTestHelper
import com.example.fbtesting.getMenuData
import com.example.fbtesting.waitUntilTimeout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class CanteenNavHostKtTest{
//
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//
//    @get:Rule(order = 1)
//    val composeRule = createAndroidComposeRule<TestActivity>()
//
//    private lateinit var navController: TestNavHostController
//
//    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
//
//    private fun assertCurrentDestinationName(expectedName:NavBackStackEntry){
//        assertEquals(expectedName.id, navController.currentBackStackEntry?.id )
//    }
//
//    //you can try make two children and call set content with right config
////    @HiltAndroidTest
////    @RunWith(AndroidJUnit4::class)
////    inner class FirstChild(){
////        @Before
////        fun setUp(){
////            composeRule.setContent {
////                FakeAndroidRepositoryHelper.testSetUserEmail(EMAIL_EXAMPLE)
////                navController = TestNavHostController(context)
////                navController.navigatorProvider.addNavigator(ComposeNavigator())
////                CanteenNavHost(navController, context)
////            }
////            }
////
////
////        @Test
////        fun secondTest(){
////            composeRule.onNodeWithText(context.getString(R.string.sign_in)).assertIsDisplayed()
////        }
//
//
////        }
//
//
//
//
//
//    @Before
//    fun setUp(){
//        composeRule.setContent {
//            FakeAndroidRepositoryHelper.testSetUserEmail(null)
//            navController = TestNavHostController(context)
//            navController.navigatorProvider.addNavigator(ComposeNavigator())
//            CanteenNavHost(navController, context)
//        }
//    }
//
//
//    @Test
//    fun hostTest(){
//        composeRule.onNodeWithText(context.getString(R.string.sign_in)).assertIsDisplayed()
//        composeRule.onNodeWithText(context.getString(R.string.sign_up)).assertIsDisplayed()
//        assertCurrentDestinationName(navController.getBackStackEntry<ScreenAuthorization>())
//
//
//        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
//        assertCurrentDestinationName(navController.getBackStackEntry<ScreenSignIn>())
//
//
//
//
////        assertCurrentDestinationName(navController.getBackStackEntry<ScreenAuthorization>())
////        Thread.sleep(2000)
//    }
//
//
//
//
//}

abstract class Parent{
    val number = 12
    abstract fun set()
}

class Child: Parent(){
    override fun set() {
        number
    }

}

abstract class CanteenHost{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    lateinit var navController: TestNavHostController

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    fun assertCurrentDestinationName(expectedName:NavBackStackEntry){
        assertEquals(expectedName.id, navController.currentBackStackEntry?.id )
    }



    abstract fun setUp()
    abstract fun reset()


}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CanteenHostUnauthorizedTest: CanteenHost(){

    @Before
    override fun setUp() {
        composeRule.setContent {

            FakeAndroidRepositoryHelper.testSetUserEmail(null)
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            CanteenNavHost(navController, context, onFinish = {})
        }
    }

    @After
    override fun reset() {
        FakeAndroidRepositoryHelper.resetData()
        FakeAuthorizationProviderTestHelper.reset()
    }

    //navigation from Auth to SignIn to Menu
    @Test
    fun signInSuccess_navigateToMenu(){
        //navigate to SignIn from Auth
        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenSignIn>())

        //input sign in data
        composeRule.onNodeWithText(HINT_ENTER_EMAIL)
            .performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(HINT_ENTER_PASS)
            .performTextInput(PASS_EXAMPLE)

        //set sing in result to Success
        FakeAuthorizationProviderTestHelper.signInSuccess(true)

        //prepare data for MenuScreen to be displayed
        FakeAndroidRepositoryHelper.testSetData(getMenuData())
        FakeAndroidRepositoryHelper.testSetIndex(11)

        //click on sign in button
        composeRule.onNode(
            !hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).performClick()

        //wait until the MenuScreen will be displayed
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onNodeWithText(context.getString(R.string.create_order)).isDisplayed()
        }

        assertCurrentDestinationName(navController.getBackStackEntry<ScreenMenu>())

        //size calculate start navGraph and current entity, so it's equal to 2
        assertTrue(navController.backStack.size ==2)


    }

    //navigate from Auth to SignIn to Forgot back to SignIn to Menu
    @Test
    fun forgotPassBackToSignIn_success_navigateToMenu(){
        //to SignIn
        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
        //to Forgot
        composeRule.onNodeWithText(context.getString(R.string.forgot_password)).performClick()
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenForgot>())

        //send forgot email is set to success
        FakeAuthorizationProviderTestHelper.sendForgotSuccess(true)

        //input data for send forgot email
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.send_email)).performClick()
        composeRule.waitForIdle()

        //assert that it navigate to SignIn
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenSignIn>())

        //enter sign in data
        composeRule.onNodeWithText(HINT_ENTER_EMAIL)
            .performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(HINT_ENTER_PASS)
            .performTextInput(PASS_EXAMPLE)

        //set sing in result to Success
        FakeAuthorizationProviderTestHelper.signInSuccess(true)

        //prepare data for MenuScreen to be displayed
        FakeAndroidRepositoryHelper.testSetData(getMenuData())
        FakeAndroidRepositoryHelper.testSetIndex(11)

        //click on sign in button
        composeRule.onNode(
            !hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).performClick()
        composeRule.waitForIdle()

        assertCurrentDestinationName(navController.getBackStackEntry<ScreenMenu>())

        //size calculate start navGraph and current entity, so it's equal to 2
        assertTrue(navController.backStack.size ==2)
    }

    //navigate from Auth to SignUp to Menu
    @Test
    fun signUpSuccess_navigateToMenu(){
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()

        //assert sign up screen and enter needed data
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenSignUp>())
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(HINT_ENTER_PASS).performTextInput(PASS_EXAMPLE)

        //set sing up result to Success
        FakeAuthorizationProviderTestHelper.signUpSuccess(true)

        //prepare data for MenuScreen to be displayed
        FakeAndroidRepositoryHelper.testSetData(getMenuData())
        FakeAndroidRepositoryHelper.testSetIndex(11)
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        composeRule.waitForIdle()


        assertCurrentDestinationName(navController.getBackStackEntry<ScreenMenu>())
        assertTrue(navController.backStack.size ==2)

    }

    //navigate from Auth to SignIn to SignUp to Menu
    @Test
    fun navigateThroughSignInToSignUp_signUpSuccess_navigateToMenuScreen(){
        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()

        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()

        //assert sign up screen and enter needed data
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenSignUp>())
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(HINT_ENTER_PASS).performTextInput(PASS_EXAMPLE)

        //set sing up result to Success
        FakeAuthorizationProviderTestHelper.signUpSuccess(true)

        //prepare data for MenuScreen to be displayed
        FakeAndroidRepositoryHelper.testSetData(getMenuData())
        FakeAndroidRepositoryHelper.testSetIndex(11)
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        composeRule.waitForIdle()


        assertCurrentDestinationName(navController.getBackStackEntry<ScreenMenu>())
        assertTrue(navController.backStack.size ==2)
    }

}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CanteenHostAuthorizedTest: CanteenHost(){

    @Before
    override fun setUp() {
        composeRule.setContent {
            FakeAndroidRepositoryHelper.testSetUserEmail(EMAIL_EXAMPLE)
            FakeAndroidRepositoryHelper.testSetData(getMenuData())
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            CanteenNavHost(navController, context, onFinish = {} )
        }
    }

    @After
    override fun reset() {
        FakeAndroidRepositoryHelper.resetData()
    }

    /*
    1)Menu -> Summary -> Status
     */



    @Test
    fun secondTest(){
        composeRule.onNodeWithText(context.getString(R.string.create_order)).assertIsDisplayed()
    }

}

