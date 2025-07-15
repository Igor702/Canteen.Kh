package com.example.fbtesting.ui

import android.content.Context
import android.util.Log
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.CanteenNavHost
import com.example.fbtesting.EDIT_RADIO_CASH_TAG
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.HINT_ENTER_PASS
import com.example.fbtesting.MENU_LIST_TAG
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.PASS_EXAMPLE
import com.example.fbtesting.R
import com.example.fbtesting.ScreenEdit
import com.example.fbtesting.ScreenForgot
import com.example.fbtesting.ScreenMenu
import com.example.fbtesting.ScreenSignIn
import com.example.fbtesting.ScreenSignUp
import com.example.fbtesting.ScreenStatus
import com.example.fbtesting.TAG
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.FakeAndroidRepositoryHelper
import com.example.fbtesting.data.authorization.FakeAuthorizationProviderTestHelper
import com.example.fbtesting.getMenuData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


abstract class Parent {
    val number = 12
    abstract fun set()
}

class Child : Parent() {
    override fun set() {
        number
    }

}

abstract class CanteenHost {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    lateinit var navController: TestNavHostController

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    fun assertCurrentDestinationName(expectedName: NavBackStackEntry) {
        assertEquals(expectedName.id, navController.currentBackStackEntry?.id)
    }


    abstract fun setUp()
    abstract fun reset()


}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CanteenHostUnauthorizedTest : CanteenHost() {

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
    fun signInSuccess_navigateToMenu() {
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
        assertTrue(navController.backStack.size == 2)


    }

    //navigate from Auth to SignIn to Forgot back to SignIn to Menu
    @Test
    fun forgotPassBackToSignIn_success_navigateToMenu() {
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
        assertTrue(navController.backStack.size == 2)
    }

    //navigate from Auth to SignUp to Menu
    @Test
    fun signUpSuccess_navigateToMenu() {
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
        assertTrue(navController.backStack.size == 2)

    }

    //navigate from Auth to SignIn to SignUp to Menu
    @Test
    fun navigateThroughSignInToSignUp_signUpSuccess_navigateToMenuScreen() {
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
        assertTrue(navController.backStack.size == 2)
    }

}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CanteenHostAuthorizedTest : CanteenHost() {

    private val lastIndex = 5

    @Before
    override fun setUp() {
        composeRule.setContent {
            FakeAndroidRepositoryHelper.testSetUserEmail(EMAIL_EXAMPLE)
            FakeAndroidRepositoryHelper.testSetData(getMenuData())
            FakeAndroidRepositoryHelper.testSetIndex(5)
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            Surface() {
                CanteenNavHost(navController, context, onFinish = {})
            }

        }
    }

    @After
    override fun reset() {
        FakeAndroidRepositoryHelper.resetData()
    }

    /*
    1)Menu -> Edit -> Status
    2)Menu -> Edit <- Menu data is cleared -> Edit new order
     */
    @Test
    fun navigateThroughMenuEditStatus_valid() {
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenMenu>())
        composeRule.onNodeWithTag(MENU_LIST_TAG).performScrollToNode(hasText(getMenuData()[5].title))
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(getMenuData()[5].title)))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()
        composeRule.waitForIdle()

        assertCurrentDestinationName(navController.getBackStackEntry<ScreenEdit>())
        composeRule.waitForIdle()

        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.increase_count))
                        and hasAnySibling(hasText(getMenuData()[5].title))
            ).performClick()

        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(EDIT_RADIO_CASH_TAG)))
            .performClick()

        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()

        composeRule.waitForIdle()

        composeRule.onRoot(useUnmergedTree = true).printToLog(TAG)
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenStatus>())
        composeRule.onNodeWithText((getMenuData()[5].price.toInt()*2).toString()).assertIsDisplayed()
        composeRule.onNodeWithText(ORDER_COOKING + (lastIndex+1)).assertIsDisplayed()


    }

    @Test
    fun navigateToEditAndBack_dataCleared(){
        composeRule.onNodeWithTag(MENU_LIST_TAG).performScrollToNode(hasText(getMenuData()[5].title))
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(getMenuData()[5].title)))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()
        composeRule.waitForIdle()

        Log.d(TAG, "navigateToEditAndBack, backStack before backPressed: ${navController.currentBackStack.value}")


        assertCurrentDestinationName(navController.getBackStackEntry<ScreenEdit>())
        composeRule.activityRule.scenario.onActivity { activity->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        Log.d(TAG, "navigateToEditAndBack, backStack after backPressed: ${navController.currentBackStack.value}")

        composeRule.waitForIdle()
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenMenu>())
        composeRule.onNodeWithTag(MENU_LIST_TAG).performScrollToNode(hasText(getMenuData()[5].title))
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(getMenuData()[5].title))).assertIsOn()

    }

    @Test
    fun pressBackFromStatus_doNotNavigateToEdit(){

        //navigate to StatusScreen
        composeRule.onNodeWithTag(MENU_LIST_TAG).performScrollToNode(hasText(getMenuData()[5].title))
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(getMenuData()[5].title)))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()
        composeRule.waitForIdle()


        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(EDIT_RADIO_CASH_TAG)))
            .performClick()

        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()

        composeRule.waitForIdle()

        assertCurrentDestinationName(navController.getBackStackEntry<ScreenStatus>())

        composeRule.activityRule.scenario.onActivity { activity->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        assertCurrentDestinationName(navController.getBackStackEntry<ScreenStatus>())

    }

}

