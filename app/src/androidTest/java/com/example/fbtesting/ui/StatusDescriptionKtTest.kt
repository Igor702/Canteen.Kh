package com.example.fbtesting.ui

import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.ORDER_READY
import com.example.fbtesting.PAY_BY_CARD
import com.example.fbtesting.R
import com.example.fbtesting.TAG
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.FakeAndroidRepositoryHelper
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class StatusDescriptionKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()


    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val dishes = mutableListOf(
        Dish(id = "1", title = "title1", price = "10", checked = true),
        Dish(id = "2", title = "title2", price = "20", checked = true),
        Dish(id = "3", title = "title3", price = "30", checked = true),
        Dish(id = "4", title = "title4", price = "40", checked = true),
        Dish(id = "5", title = "title5", price = "50", checked = true),

        )
    private var orderString = ""
    private var ordersTotalPrice = ""

    private val testIndex = 5
    private val updatedIndex = 6


    private var onExit = false

    @Before
    fun setUp() {
        composeRule.setContent {
            FakeAndroidRepositoryHelper.testSetIndex(testIndex)


            val viewModel = hiltViewModel<SharedViewModel>()
            viewModel.setDishes(dishes)
            viewModel.getChosenDishes()
            viewModel.sendOrder(
                viewModel.getInitPrice().toString(),
                PAY_BY_CARD
            )
            orderString = viewModel.getStatusOrderDishesWithCountString()
            ordersTotalPrice = viewModel.getStatusOrderTotalPrice()



            MaterialTheme {
                Surface {
                    StatusScreen(viewModel = viewModel, onExit = { onExit = !onExit })
                }
            }
            Log.d(TAG, "StatusTest ")
        }
    }


    @After
    fun tearDown() {
        onExit = false
        FakeAndroidRepositoryHelper.resetData()
    }

    /*
1 all ui elements are visible
2 changes to orderStatus are visible in ui
3 press exit onExit triggered
 */

    @Test
    fun allUiElementsAreVisible() {
        composeRule.onNodeWithText(context.getString(R.string.your_order)).assertIsDisplayed()
        composeRule.onNodeWithText(orderString).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.total_price_is)).assertIsDisplayed()
        composeRule.onNodeWithText(ordersTotalPrice).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.pay_by)).assertIsDisplayed()
        composeRule.onNodeWithText(PAY_BY_CARD).assertIsDisplayed()
        composeRule.onNodeWithText(ORDER_COOKING + updatedIndex).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.exit)).assertIsDisplayed()
    }

    @Test
    fun notifyStatusChanges_changesAreVisibleInUi() {

        composeRule.onNodeWithText(ORDER_COOKING + updatedIndex).assertIsDisplayed()
        FakeAndroidRepositoryHelper.notifyChanges(ORDER_READY)
        composeRule.onNodeWithText(ORDER_COOKING + updatedIndex).assertIsNotDisplayed()
        composeRule.onNodeWithText(ORDER_READY + updatedIndex).assertIsDisplayed()


    }

    @Test
    fun pressExitButton_onExitIsCalled() {
        composeRule.onNodeWithText(context.getString(R.string.exit)).performClick()

        assertTrue(onExit)

    }

}