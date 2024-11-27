package com.example.fbtesting.ui.authorization

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthorizationDescriptionKtTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext



    @Test
    fun authorizationScreen_validUser_onNavigateToMenuCalled(){
        var navigatedToMenu = false
        composeRule.setContent {
            AuthorizationScreen(
                onNavigateToMenu = {navigatedToMenu = true},
                onNavigateToSignIn = {},
                onNavigateToSignUp = {},
                currentUser = EMAIL_EXAMPLE
            )
        }



        composeRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertDoesNotExist()

        assertTrue(navigatedToMenu)
    }

    @Test
    fun authorizationScreen_emptyUser_allUiElementsAreVisibleAndNavigateToMenuWasNotCalled(){

        var navigatedToMenu = false
        composeRule.setContent {
            AuthorizationScreen(
                onNavigateToMenu = {navigatedToMenu = true},
                onNavigateToSignIn = {},
                onNavigateToSignUp = {},
                currentUser = ""
            )
        }
        assertFalse(navigatedToMenu)

        composeRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()
    }


    @Test
    fun authorizationScreen_nullUser_allUiElementsAreVisibleAndNavigateToMenuWasNotCalled(){

        var navigatedToMenu = false
        composeRule.setContent {
            AuthorizationScreen(
                onNavigateToMenu = {navigatedToMenu = true},
                onNavigateToSignIn = {},
                onNavigateToSignUp = {},
                currentUser = ""
            )
        }
        assertFalse(navigatedToMenu)
        composeRule.onRoot(useUnmergedTree = true).printToLog("signInTest")

        composeRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()
    }

    @Test
    fun authorizationScreen_emptyUser_pressSignInNavigateToSignInScreen(){

        var navigatedToMenu = false
        var navigatedToSignIn = false
        var navigatedToSingUp = false
        composeRule.setContent {
            AuthorizationScreen(
                onNavigateToMenu = {navigatedToMenu = true},
                onNavigateToSignIn = {navigatedToSignIn = true},
                onNavigateToSignUp = {navigatedToSingUp = true},
                currentUser = ""
            )
        }


        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
        assertFalse(navigatedToMenu)
        assertFalse(navigatedToSingUp)
        assertTrue(navigatedToSignIn)

    }

    @Test
    fun authorizationScreen_emptyUser_pressSignUpNavigateToSignUpScreen(){

        var navigatedToMenu = false
        var navigatedToSignIn = false
        var navigatedToSingUp = false
        composeRule.setContent {
            AuthorizationScreen(
                onNavigateToMenu = {navigatedToMenu = true},
                onNavigateToSignIn = {navigatedToSignIn = true},
                onNavigateToSignUp = {navigatedToSingUp = true},
                currentUser = ""
            )
        }


        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        assertFalse(navigatedToMenu)
        assertTrue(navigatedToSingUp)
        assertFalse(navigatedToSignIn)

    }




}