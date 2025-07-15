package com.example.fbtesting.ui.order

import android.content.Context
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
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.ORDER_READY
import com.example.fbtesting.ORDER_STATUS_FALSE
import com.example.fbtesting.PAY_BY_CARD
import com.example.fbtesting.R
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.FakeAndroidRepositoryHelper
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.convertOrderToString
import com.example.fbtesting.getMenuData
import com.example.fbtesting.view_model.order.StatusViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertFalse
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
    private val dishes = getMenuData()

    private val totalPrice = "125"
    private fun getDishesForOrder(): MutableMap<String, Int> {
        val dishesForOrder = mutableMapOf<String, Int>()
        dishes.map { it.title }.forEach {
            dishesForOrder[it] = 1

        }
        return dishesForOrder
    }

    private val serializedOrder = Json.encodeToString(
        Order(
            dishes = getDishesForOrder(),
            payBy = PAY_BY_CARD,
            currentUser = EMAIL_EXAMPLE,
            orderStatus = ORDER_STATUS_FALSE,
            totalPrice = totalPrice
        )
    )

    private val deserializedOrder = Json.decodeFromString<Order>(serializedOrder)
    private val testIndex = 5

    private var onExit = false
    private var onSecondPress = false

    @Before
    fun setUp() {
        composeRule.setContent {
            FakeAndroidRepositoryHelper.testSetIndex(testIndex)

            val viewModel = hiltViewModel<StatusViewModel>()

            MaterialTheme {
                Surface {
                    StatusScreen(
                        viewModel = viewModel,
                        onExit = { onExit = !onExit },
                        serializedOrder = serializedOrder,
                        onNotifySecondPressToExit = {onSecondPress = !onSecondPress}
                    )
                }
            }
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
        composeRule.waitForIdle()
        composeRule.onNodeWithText(context.getString(R.string.your_order)).assertIsDisplayed()
        composeRule.onNodeWithText(deserializedOrder.dishes.convertOrderToString())
            .assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.total_price_is)).assertIsDisplayed()
        composeRule.onNodeWithText(deserializedOrder.totalPrice).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.pay_by)).assertIsDisplayed()
        composeRule.onNodeWithText(PAY_BY_CARD).assertIsDisplayed()
        composeRule.onNodeWithText(ORDER_COOKING + testIndex).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.exit)).assertIsDisplayed()
    }

    @Test
    fun notifyStatusChanges_changesAreVisibleInUi() {

        composeRule.onNodeWithText(ORDER_COOKING + testIndex).assertIsDisplayed()
        FakeAndroidRepositoryHelper.notifyChanges(ORDER_READY)
        composeRule.onNodeWithText(ORDER_COOKING + testIndex).assertIsNotDisplayed()
        composeRule.onNodeWithText(ORDER_READY + testIndex).assertIsDisplayed()


    }

    @Test
    fun pressExitButton_onExitIsCalled() {
        composeRule.onNodeWithText(context.getString(R.string.exit)).performClick()

        assertTrue(onExit)

    }

    @Test
    fun pressBack_onNotifySecondPressToExitCalled(){
        assertFalse(onSecondPress)
        composeRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        assertTrue(onSecondPress)
    }

}