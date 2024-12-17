package com.example.fbtesting.ui

import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
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
import com.example.fbtesting.MAX_COUNT_DISH
import com.example.fbtesting.MIN_COUNT_DISH
import com.example.fbtesting.R
import com.example.fbtesting.SUMMARY_LIST_TAG
import com.example.fbtesting.SUMMARY_RADIO_CARD_TAG
import com.example.fbtesting.SUMMARY_RADIO_CASH_TAG
import com.example.fbtesting.TAG
import com.example.fbtesting.TestActivity
import com.example.fbtesting.assertFalseForAll
import com.example.fbtesting.data.FakeAndroidRepositoryHelper
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.view_model.SharedViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SummaryDescriptionKtTest {

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

    private var onNavigate = false
    private var onCancel = false
    private var onNoFood = false
    private var onNoPayment = false
    private var onMax = false
    private var onMin = false


    //todo:create separate object for these methods for encapsulation
    private var totalPrice = 0
    private val countOfDishes = mutableMapOf<String, Int>()

    private fun getCurrentTotalPrice(): Int {
        totalPrice = 0
        countOfDishes.forEach { it ->
            totalPrice += (it.value * it.key.toInt())
        }
        Log.d(TAG, "getTotalPrice: $totalPrice")
        return totalPrice
    }

    private fun initDishesWithCount() {
        for (dish in dishes) {
            countOfDishes[dish.price] = 1
        }
    }

    private fun onPlus(dishPrice: String) {
        var count = countOfDishes[dishPrice] ?: MAX_COUNT_DISH

        if (count <= MAX_COUNT_DISH) {
            count++
            Log.d(TAG, "onPlus count of dish: $dishPrice, $count")
            countOfDishes.remove(dishPrice)
            countOfDishes[dishPrice] = count
        }
    }

    private fun onMinus(dishPrice: String) {
        var count = countOfDishes[dishPrice] ?: MIN_COUNT_DISH

        //create variable for count
        if (count != MIN_COUNT_DISH) {
            count--
            Log.d(TAG, "onMinus count of dish: $dishPrice, $count")
            countOfDishes.remove(dishPrice)
            countOfDishes[dishPrice] = count
        }
    }


    @Before
    fun setUp() {
        composeRule.setContent {
            FakeAndroidRepositoryHelper.testSetIndex(5)
            initDishesWithCount()

            val viewModel = hiltViewModel<SharedViewModel>()
            viewModel.setDishes(dishes)
            MaterialTheme {
                Surface {
                    OrdersSummaryScreen(viewModel = viewModel,
                        onNavigateToStatusFragment = { onNavigate = !onNavigate },
                        onCancel = { onCancel = !onCancel },
                        onNotifyNoFood = { onNoFood = !onNoFood },
                        onNotifyNoPaymentMethod = { onNoPayment = !onNoPayment },
                        onNotifyMaxDishCount = {
                            onMax = !onMax
                            Log.d(TAG, "onMax: $onMax")
                        },
                        onNotifyMinDishCount = {
                            onMin = !onMin
                            Log.d(TAG, "onMax: $onMax")
                        })
                }
            }
            Log.d(TAG, "SummaryTest ")
        }
    }


    @After
    fun tearDown() {
        totalPrice = 0
        countOfDishes.clear()
        FakeAndroidRepositoryHelper.resetData()
    }


    /*
   1 check ui elements
   2 test increase and decrease btn and appropriate price count
   3 test notify min and max count called price is still max or min
   4 set all count to 0 create order onNotifyNoFoodCalled
   5 valid order no payment create order noPaymentMethod called
   6 test pay by
   7 cancel btn onCancelCalled
    */


    @Test
    fun allUiElementsAreVisible() {

        composeRule.onRoot(useUnmergedTree = true).printToLog(TAG)

        composeRule.onNodeWithText(dishes[0].title).assertIsDisplayed()
        composeRule.onNodeWithText(dishes[0].price).assertIsDisplayed()
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.increase_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.decrease_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()
        composeRule
            .onNode(
                hasText(countOfDishes[dishes[0].price].toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.total_price_is)).assertIsDisplayed()
        composeRule.onNodeWithText(getCurrentTotalPrice().toString()).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.pay_by)).assertIsDisplayed()
        composeRule.onNodeWithTag(SUMMARY_RADIO_CARD_TAG).assertIsDisplayed()
        composeRule.onNodeWithTag(SUMMARY_RADIO_CASH_TAG).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.create_order)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.cancel)).assertIsDisplayed()

        assertFalseForAll(onMax, onMin, onCancel, onNoFood, onNavigate, onNoPayment)


    }

    //   2 test increase and decrease btn and appropriate price count

    @Test
    fun increaseAndDecreaseButtons_appropriatePriceAndCountChanges() {
        //press increase in 0 node
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.increase_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).performClick().also {
                onPlus(dishes[0].price)

            }


        Log.d(TAG, "increaseTest, count: ${countOfDishes[dishes[0].price]}")

        composeRule
            .onNode(
                hasText(countOfDishes[dishes[0].price].toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()

        composeRule.onNodeWithText(getCurrentTotalPrice().toString()).assertIsDisplayed()

        //press decrease in 0 node


        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.decrease_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).performClick().also {
                onMinus(dishes[0].price)

            }

        composeRule
            .onNode(
                hasText(countOfDishes[dishes[0].price].toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()
        composeRule.onNodeWithText(getCurrentTotalPrice().toString()).assertIsDisplayed()

        //press decrease in 2 node
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.decrease_count))
                        and hasAnySibling(hasText(dishes[2].title))
            ).performClick().also {
                onMinus(dishes[2].price)

            }

        composeRule
            .onNode(
                hasText(countOfDishes[dishes[2].price].toString())
                        and hasAnySibling(hasText(dishes[2].title))
            ).assertIsDisplayed()
        composeRule.onNodeWithText(getCurrentTotalPrice().toString()).assertIsDisplayed()

    }

    //       3 test notify min and max count called price is still max or min
    @Test
    fun presIncreaseMultipleTimes_assertMaxCountAndPriceStillRightAndOnMaxFunCalled() {
        var count = 1
        val startPrice = getCurrentTotalPrice()
        //increase count and price but don't call onNotifyMaxCount

        while (count <= MAX_COUNT_DISH) {
            count++

            //press increase in 0 node
            composeRule
                .onNode(
                    hasContentDescription(context.getString(R.string.increase_count))
                            and hasAnySibling(hasText(dishes[0].title))
                ).performClick().also {
                    onPlus(dishes[0].price)

                }
        }

        //asser that onNotifyMaxCount don't called, visible count is 13
        //and take maxPrice
        composeRule
            .onNode(
                hasText(13.toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()

        assertFalse(onMax)
        assertTrue(count == 13)

        val maxPrice = getCurrentTotalPrice()

        //press increase one more time
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.increase_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).performClick().also {
                onPlus(dishes[0].price)
            }


        //call to onMax true, count and price are still the same
        composeRule
            .onNode(
                hasText(13.toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()

        assertTrue(maxPrice == getCurrentTotalPrice())
        assertTrue(onMax)


        //decrease to count 1
        while (count > 1) {
            count--

            //press decrease in 0 node
            composeRule
                .onNode(
                    hasContentDescription(context.getString(R.string.decrease_count))
                            and hasAnySibling(hasText(dishes[0].title))
                ).performClick().also {
                    onMinus(dishes[0].price)

                }
        }

        //count is equal to 1, startPrice and currentPrice are equal, onMin don't called
        assertTrue(count == 1)
        assertTrue(startPrice == getCurrentTotalPrice())
        assertFalse(onMin)

        //press one more time to set count to 0
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.decrease_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).performClick().also {
                onMinus(dishes[0].price)

            }


        //assert that count == 0, startPrice bigger than current minPrice, onMin don't called yet
        composeRule
            .onNode(
                hasText(0.toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()
        val minPrice = getCurrentTotalPrice()
        assertTrue(startPrice > minPrice)
        assertFalse(onMin)

        //decrease one more time for call onMin and check price and count
        composeRule
            .onNode(
                hasContentDescription(context.getString(R.string.decrease_count))
                        and hasAnySibling(hasText(dishes[0].title))
            ).performClick().also {
                onMinus(dishes[0].price)

            }

        //assert that count still 0, onMin called, currentPrice is still equal to minPrice
        composeRule
            .onNode(
                hasText(0.toString())
                        and hasAnySibling(hasText(dishes[0].title))
            ).assertIsDisplayed()
        assertTrue(onMin)
        assertTrue(minPrice == getCurrentTotalPrice())

        assertFalseForAll(onCancel, onNoFood, onNavigate, onNoPayment)


    }


    //   4 set all count to 0 create order onNotifyNoFoodCalled, price 0
    @Test
    fun setAllCountsToZero_createOrderCallOnNotifyNoFoodCalled() {

        //set all counts to 0
        var counter = dishes.size
        while (counter > 0) {
            counter--
            composeRule.onNodeWithTag(SUMMARY_LIST_TAG)
                .performScrollToNode(hasText(dishes[counter].title))
            composeRule
                .onNode(
                    hasContentDescription(context.getString(R.string.decrease_count))
                            and hasAnySibling(hasText(dishes[counter].title))
                ).performClick().also {
                    onMinus(dishes[counter].price)
                }
        }

        //assert that currentPrice is 0,
        assertTrue(getCurrentTotalPrice() == 0)

        //press create order btn
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()
        assertTrue(onNoFood)

        assertFalseForAll(onMax, onMin, onCancel, onNavigate, onNoPayment)


    }

    //   5 valid order no payment create order noPaymentMethod called
    @Test
    fun validOrderWithoutPaymentMethod_createNoPaymentCalled() {
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()

        assertTrue(onNoPayment)
        assertFalseForAll(onMax, onMin, onCancel, onNoFood, onNavigate)

    }

    //   6 test pay by
    @Test
    fun pressPaymentMethods_createOrder_onNavigateCalled() {
        //click on cash rbtn, cash is selected, card isn't selected
        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(SUMMARY_RADIO_CASH_TAG)))
            .performClick()
        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(SUMMARY_RADIO_CASH_TAG)))
            .assertIsSelected()
        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(SUMMARY_RADIO_CARD_TAG)))
            .assertIsNotSelected()

        //click on card rbtn, card is selected, cash isn't selected
        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(SUMMARY_RADIO_CARD_TAG)))
            .performClick()
        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(SUMMARY_RADIO_CARD_TAG)))
            .assertIsSelected()
        composeRule
            .onNode(hasClickAction() and hasParent(hasTestTag(SUMMARY_RADIO_CASH_TAG)))
            .assertIsNotSelected()

        //click create order
        composeRule.onNodeWithText(context.getString(R.string.create_order)).performClick()

        //call onNavigate true, another methods are false
        assertTrue(onNavigate)
        assertFalseForAll(onMax, onMin, onCancel, onNoFood, onNoPayment)

    }

    //   7 cancel btn onCancelCalled
    @Test
    fun cancelBtn_onCancelCalled() {
        composeRule.onNodeWithText(context.getString(R.string.cancel)).performClick()

        assertTrue(onCancel)
        assertFalseForAll(onMax, onMin, onNoFood, onNoPayment, onNavigate)


    }
}