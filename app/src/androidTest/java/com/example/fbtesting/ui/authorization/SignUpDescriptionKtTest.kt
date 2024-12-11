package com.example.fbtesting.ui.authorization

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.HINT_ENTER_PASS
import com.example.fbtesting.PASS_EXAMPLE
import com.example.fbtesting.R
import com.example.fbtesting.TestActivity
import com.example.fbtesting.data.authorization.FakeProviderTestHelper
import com.example.fbtesting.view_model.authorization.SignUpViewModel
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
class SignUpDescriptionKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var onNavigateCalled = false
    private var onNotifyEmptyFieldsCalled = false
    private var onNotifyErrorCalled = false

    @Before
    fun setUp() {
        composeRule.setContent {
            SignUpScreen(
                windowSizeClass = currentWindowAdaptiveInfo(),
                viewModel = hiltViewModel<SignUpViewModel>(),
                onNavigateToMenu = {
                    onNavigateCalled = !onNavigateCalled

                },
                onNotifyToastEmptyFields = {
                    onNotifyEmptyFieldsCalled = !onNotifyEmptyFieldsCalled
                },
                onNotifyError = {
                    onNotifyErrorCalled = !onNotifyErrorCalled

                }


            )
        }
    }

    private fun reset() {
        onNavigateCalled = false
        onNotifyEmptyFieldsCalled = false
        onNotifyErrorCalled = false
    }

    @After
    fun tearDown() {
        reset()
        FakeProviderTestHelper.reset()
    }


    /*
    --1 all ui elements are visible
    --2 empty email onNotifyEmptyFields true
      empty pass onNotifyEmptyFields true
      empty both email and pass onNotifyEmptyFields true
    --3 valid data signUpSuccess true onNavigateToMenuCalled true
    --4 valid data signUpSuccess false onNotifyError true
     */

    @Test
    fun allUiElementsAreVisible() {
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).assertIsDisplayed()
        composeRule.onNodeWithText(HINT_ENTER_PASS).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).assertIsDisplayed()

        assertFalse(onNavigateCalled)
        assertFalse(onNotifyEmptyFieldsCalled)
        assertFalse(onNotifyErrorCalled)
    }


    @Test
    fun emptyEmailOrPassOrBoth_onNotifyEmptyFieldsCalledTrue() {
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()

        assertTrue(onNotifyEmptyFieldsCalled)

        assertFalse(onNavigateCalled)
        assertFalse(onNotifyErrorCalled)
        reset()
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextClearance()


        composeRule.onNodeWithText(HINT_ENTER_PASS).performTextInput(PASS_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        assertTrue(onNotifyEmptyFieldsCalled)

        assertFalse(onNavigateCalled)
        assertFalse(onNotifyErrorCalled)
        reset()
        composeRule.onNodeWithText(HINT_ENTER_PASS).performTextClearance()


        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        assertTrue(onNotifyEmptyFieldsCalled)

        assertFalse(onNavigateCalled)
        assertFalse(onNotifyErrorCalled)

    }

    @Test
    fun validData_signUpSuccess_onNavigateCalledTrue() {
        FakeProviderTestHelper.signUpSuccess(true)

        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(HINT_ENTER_PASS).performTextInput(PASS_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        composeRule.waitForIdle()

        assertTrue(onNavigateCalled)

        assertFalse(onNotifyEmptyFieldsCalled)
        assertFalse(onNotifyErrorCalled)


    }

    @Test
    fun validData_signUpError_onNotifyErrorCalledTrue() {
        FakeProviderTestHelper.signUpSuccess(false)

        composeRule.onNodeWithText(HINT_ENTER_EMAIL).performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(HINT_ENTER_PASS).performTextInput(PASS_EXAMPLE)
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        composeRule.waitForIdle()

        assertTrue(onNotifyErrorCalled)

        assertFalse(onNavigateCalled)
        assertFalse(onNotifyEmptyFieldsCalled)


    }


}