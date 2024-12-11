package com.example.fbtesting.ui.authorization

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.R
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.authorization.FakeProviderTestHelper
import com.example.fbtesting.view_model.authorization.ForgotPassViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ForgotDescriptionKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private var onNavigateSignInCalled = false
    private var onNotifyEmptyFieldsCalled = false
    private var onNotifyErrorCalled = false

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext


    /*
    --1 all ui elements are visible
    --2 valid data navigateToSignIn called
    --3 empty field onNotifyToastEmptyField called
    --4 valid data fail to load data onNavigate don't called, on notify user called
     */
    @Before
    fun setUp() {
        composeRule.setContent {
            ForgotScreen(viewModel = hiltViewModel<ForgotPassViewModel>(),
                onNavigateToSignIn = {
                    onNavigateSignInCalled = !onNavigateSignInCalled
                },
                onNotifyToastEmptyField = {
                    onNotifyEmptyFieldsCalled = !onNotifyEmptyFieldsCalled
                },
                onNotifyError = {
                    onNotifyErrorCalled = !onNotifyErrorCalled
                })
        }
    }


    @After
    fun tearDown() {
        onNavigateSignInCalled = false
        onNotifyEmptyFieldsCalled = false
        onNotifyErrorCalled = false
        FakeProviderTestHelper.reset()
    }


    @Test
    fun allUiElementsAreVisible() {
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.send_email)).assertIsDisplayed()

    }

    @Test
    fun validData_sendSuccess_onNavigateToSignInTrue() {


        FakeProviderTestHelper.sendForgotSuccess(true)

        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.send_email)).performClick()
        composeRule.waitForIdle()


        assertTrue(onNavigateSignInCalled)

        assertFalse(onNotifyEmptyFieldsCalled)
        assertFalse(onNotifyErrorCalled)
    }

    @Test
    fun emptyField_onNotifyEmptyFieldCalled() {
        composeRule.onNodeWithText(context.getString(R.string.send_email)).performClick()

        assertTrue(onNotifyEmptyFieldsCalled)

        assertFalse(onNavigateSignInCalled)
        assertFalse(onNotifyErrorCalled)

    }

    @Test
    fun validData_sendError_onNotifyErrorCalled() {
        FakeProviderTestHelper.sendForgotSuccess(false)

        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.send_email)).performClick()
        composeRule.waitForIdle()

        assertTrue(onNotifyErrorCalled)

        assertFalse(onNotifyEmptyFieldsCalled)
        assertFalse(onNavigateSignInCalled)

    }


}