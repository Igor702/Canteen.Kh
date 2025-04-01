package com.example.fbtesting.ui.order

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.MENU_LIST_TAG
import com.example.fbtesting.R
import com.example.fbtesting.TAG
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.FakeAndroidRepositoryHelper
import com.example.fbtesting.getMenuData
import com.example.fbtesting.view_model.order.MenuViewModel
import com.example.fbtesting.waitUntilTimeout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/*
   1 data load success all elements are visible
   2 data load error onNotifyError called
   3 scroll to position, check, create order, onNavigate true
   4 check and uncheck, create order, onNotifyEmptyOrder true

    */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MenuDescriptionKtTest {


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private var onNavigateSummaryCalled = false
    private var onNotifyEmptyOrderCalled = false
    private var onNotifyErrorCalled = false
    private var onFinishCalled = false

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val list = getMenuData()


    @Before
    fun setUp() {
        composeRule.setContent {
            Log.d(TAG, "MenuTestSuccess setContent")
            FakeAndroidRepositoryHelper.testSetData(list)
            MenuScreen(viewModel = hiltViewModel<MenuViewModel>(),
                onNavigateToSummary = { onNavigateSummaryCalled = !onNavigateSummaryCalled },
                onNotifyEmptyOrder = { onNotifyEmptyOrderCalled = !onNotifyEmptyOrderCalled },
                onNotifyLoadingError = { onNotifyErrorCalled = !onNotifyErrorCalled },
                onFinish = { onFinishCalled = !onFinishCalled }
            )
        }
    }


    @After
    fun tearDown() {
        onNavigateSummaryCalled = false
        onNotifyEmptyOrderCalled = false
        onNotifyErrorCalled = false
        onFinishCalled = false
        FakeAndroidRepositoryHelper.resetData()
    }


    @Test
    fun dataLoadSuccess_uiElementsAreVisible() {
        composeRule.onRoot(useUnmergedTree = true).printToLog(TAG)
        composeRule.onNodeWithText(list[0].title).assertIsDisplayed()
        composeRule.onNodeWithText(list[0].price).assertIsDisplayed()
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(list[0].title)))
            .assertIsDisplayed()
        composeRule
            .onNode(
                hasText(context.getString(R.string.add_to_basket)) and hasAnySibling(
                    hasText(
                        list[0].title
                    )
                )
            ).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).assertIsDisplayed()

    }


    @Test
    fun scrollToPositionCheckCreateOrder_onNavigateSummaryCalledTrue() {
        composeRule.onNodeWithTag(MENU_LIST_TAG).performScrollToNode(hasText(list[5].title))
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(list[5].title)))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()
        composeRule.waitForIdle()

        assertTrue(onNavigateSummaryCalled)

        assertFalse(onNotifyEmptyOrderCalled)
        assertFalse(onNotifyErrorCalled)

    }

    @Test
    fun scrollToPositionCheckUncheckCreateOrder_onNotifyEmptyOrderCalledTrue() {
        composeRule.onNodeWithTag(MENU_LIST_TAG).performScrollToNode(hasText(list[5].title))
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(list[5].title)))
            .performClick()
        composeRule.onNode(hasClickAction() and hasAnySibling(hasText(list[5].title)))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()

        assertTrue(onNotifyEmptyOrderCalled)

        assertFalse(onNavigateSummaryCalled)
        assertFalse(onNotifyErrorCalled)
    }

    @Test
    fun doublePressBackButton_onFinishCalled() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
            activity.onBackPressedDispatcher.onBackPressed()
        }
        assertTrue(onFinishCalled)
    }

    //if user press back slower than 2s, than onFinish won't called
    @Test
    fun pressManyTimesWithDelay_onFinishedNotCalled() {
        //first press back
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        //wait for pressSecondTime set to false in MenuDescription
        composeRule.waitUntilTimeout(2100)


        //second press back
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        assertFalse(onFinishCalled)

        //wait for pressSecondTime set to false in MenuDescription
        composeRule.waitUntilTimeout(2100)

        //third press back
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        assertFalse(onFinishCalled)

    }

}


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MenuDescriptionLoadErrorKtTest {


    @get:Rule(order = 0)
    val mHiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private var onNavigateSummaryCalled = false
    private var onNotifyEmptyOrderCalled = false
    private var onNotifyErrorCalled = false


    @Before
    fun setUp() {
        composeRule.setContent {
            Log.d(TAG, "MenuTestError setContent")

            FakeAndroidRepositoryHelper.testSetData(null)

            MenuScreen(viewModel = hiltViewModel<MenuViewModel>(),
                onNavigateToSummary = { onNavigateSummaryCalled = !onNavigateSummaryCalled },
                onNotifyEmptyOrder = { onNotifyEmptyOrderCalled = !onNotifyEmptyOrderCalled },
                onNotifyLoadingError = { onNotifyErrorCalled = !onNotifyErrorCalled },
                onFinish = {
                    //tested up
                }
            )
        }
    }


    @After
    fun tearDown() {
        onNavigateSummaryCalled = false
        onNotifyEmptyOrderCalled = false
        onNotifyErrorCalled = false
        FakeAndroidRepositoryHelper.resetData()
    }

    @Test
    fun dataLoadError_onNotifyErrorCalledTrue() {
        assertTrue(onNotifyErrorCalled)

        assertFalse(onNavigateSummaryCalled)
        assertFalse(onNotifyEmptyOrderCalled)
    }


}



