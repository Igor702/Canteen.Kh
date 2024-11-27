package com.example.fbtesting.ui.authorization

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.core.app.ActivityOptionsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.HINT_ENTER_EMAIL
import com.example.fbtesting.HINT_ENTER_PASS
import com.example.fbtesting.PASS_EXAMPLE
import com.example.fbtesting.R
import com.example.fbtesting.SIGN_IN_SCREEN_STATELESS_TAG
import com.example.fbtesting.view_model.authorization.SignInViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SignInDescriptionKtTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private var onSignInCalled = false
    private var onForgotPasswordCalled = false
    private var onSignInWithGoogleCalled = false
    private var onSignUpCalled = false
    private var onEmptyFieldsCalled = false

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext





    @Before
    fun setUpScreen(){


        composeRule.setContent {


            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            SignInScreen(
                windowSizeClass = currentWindowAdaptiveInfo(),
                email = email,
                onEmailChanged = { newEmail: String -> email = newEmail },
                password = password,
                onPasswordChanged = { newPass: String -> password = newPass },
                onSignIn = {onSignInCalled = !onSignInCalled},
                onForgotPassword = {onForgotPasswordCalled = !onForgotPasswordCalled},
                onSignInWithGoogle = {onSignInWithGoogleCalled = !onSignInWithGoogleCalled},
                onSignUp = {onSignUpCalled = !onSignUpCalled},
                onNotifyToastEmptyFields = {onEmptyFieldsCalled = !onEmptyFieldsCalled}

            )
        }
    }

    @After
    fun tearDownInvocationData(){
        onSignInCalled = false
        onForgotPasswordCalled = false
        onSignInWithGoogleCalled = false
        onSignUpCalled = false
        onEmptyFieldsCalled = false
    }


    @Test
    fun signInScreen_uiIsVisible(){
        composeRule.onRoot(useUnmergedTree = true).printToLog("signInTest")
        composeRule.onNodeWithText(HINT_ENTER_EMAIL).assertExists()
        composeRule.onNodeWithText(HINT_ENTER_PASS).assertExists()
        //assertion for button "sign in"
        composeRule.onNode(
            !hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).assertExists()

        composeRule.onNodeWithText(context.getString(R.string.forgot_password)).assertExists()

        //assertion for node sign in with google with text "sign in"
        composeRule.onNode(
            hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).assertExists()
        composeRule.onNodeWithText(context.getString(R.string.don_t_have_an_account)).assertExists()
        composeRule.onNodeWithText(context.getString(R.string.sign_up)).assertExists()

    }

    @Test
    fun singInScreen_validEmailAndPass_emailAndPassAreVisible(){

        composeRule.
        onNodeWithText(HINT_ENTER_EMAIL)
            .performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(EMAIL_EXAMPLE).assertIsDisplayed()
        composeRule.
        onNodeWithText(HINT_ENTER_PASS)
            .performTextInput(PASS_EXAMPLE)
        composeRule.onNodeWithText(PASS_EXAMPLE).assertIsDisplayed()

    }



    @Test
    fun singInScreen_validEmailAndPassClickSingInButton_onSignInCalled(){

        composeRule.
        onNodeWithText(HINT_ENTER_EMAIL)
            .performTextInput(EMAIL_EXAMPLE)
        composeRule.onNodeWithText(EMAIL_EXAMPLE).assertIsDisplayed()
        composeRule.
        onNodeWithText(HINT_ENTER_PASS)
            .performTextInput(PASS_EXAMPLE)
        composeRule.onNodeWithText(PASS_EXAMPLE).assertIsDisplayed()


        //click on sign in button
        composeRule.onNode(
            !hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).performClick()

        assertTrue(onSignInCalled)
    }

    @Test
    fun singInScreen_emptyEmailAndPassClickSingInButton_onSignInNotCalledOnNotifyEmptyFieldsCalled(){

        //click on sign in button
        composeRule.onNode(
            !hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).performClick()

        assertFalse(onSignInCalled)
        assertTrue(onEmptyFieldsCalled)
    }


    @Test
    fun signInScreen_forgotPassSignInWithGoogleSignUpClick_appropriateMethodsCalled(){

        composeRule.onNodeWithText(context.getString(R.string.forgot_password)).performClick()

        composeRule.onNode(
            hasContentDescription(context.getString(R.string.sign_in_with_google))
                    and hasText(context.getString(R.string.sign_in))
        ).performClick()

        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()


        assertTrue(onForgotPasswordCalled)
        assertTrue(onSignInWithGoogleCalled)
        assertTrue(onSignUpCalled)
    }


}