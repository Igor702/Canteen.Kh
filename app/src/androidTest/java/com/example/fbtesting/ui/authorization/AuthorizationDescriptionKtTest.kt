package com.example.fbtesting.ui.authorization

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.fbtesting.AUTHORIZATION_CONTENT_TAG
import com.example.fbtesting.EMAIL_EXAMPLE
import com.example.fbtesting.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthorizationDescriptionKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private fun setAuthContent(
        onNavigateToMenu: () -> Unit,
        onNavigateToSignIn: () -> Unit,
        onNavigateToSignUp: () -> Unit,
        currentUser: String?
    ){
        composeRule.setContent {
            AuthorizationScreen(
                onNavigateToMenu = { onNavigateToMenu() },
                onNavigateToSignIn = {onNavigateToSignIn()},
                onNavigateToSignUp = {onNavigateToSignUp()},
                currentUser = currentUser
            )
        }
    }

    private var navigatedToMenu = false
    private var navigatedToSignIn = false
    private var navigatedToSignUp = false

    @After
    fun reset(){
        navigatedToMenu = false
        navigatedToSignIn = false
        navigatedToSignUp = false
    }


    @Test
    fun authorizationScreen_validUser_onNavigateToMenuCalled() {

        setAuthContent(
            onNavigateToMenu = {navigatedToMenu = true},
            onNavigateToSignIn = {},
            onNavigateToSignUp = {},
            currentUser = EMAIL_EXAMPLE)


        composeRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertDoesNotExist()

        assertTrue(navigatedToMenu)
    }

    @Test
    fun authorizationScreen_emptyUser_allUiElementsAreVisibleAndNavigateToMenuWasNotCalled() {

        setAuthContent(
            onNavigateToMenu = {navigatedToMenu = true},
            onNavigateToSignIn = {},
            onNavigateToSignUp = {},
            currentUser = "")

        assertFalse(navigatedToMenu)

        composeRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()
    }


    @Test
    fun authorizationScreen_nullUser_allUiElementsAreVisibleAndNavigateToMenuWasNotCalled() {

        setAuthContent(
            onNavigateToMenu = {navigatedToMenu = true},
            onNavigateToSignIn = {},
            onNavigateToSignUp = {},
            currentUser = null
        )
        assertFalse(navigatedToMenu)
        composeRule.onNodeWithTag(AUTHORIZATION_CONTENT_TAG).assertIsDisplayed()
    }

    @Test
    fun authorizationScreen_emptyUser_pressSignInNavigateToSignInScreen() {

        setAuthContent(
            onNavigateToMenu = { navigatedToMenu = true },
            onNavigateToSignIn = { navigatedToSignIn = true },
            onNavigateToSignUp = { navigatedToSignUp = true },
            currentUser = "")


        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
        assertFalse(navigatedToMenu)
        assertFalse(navigatedToSignUp)
        assertTrue(navigatedToSignIn)

    }

    @Test
    fun authorizationScreen_emptyUser_pressSignUpNavigateToSignUpScreen() {

        setAuthContent(
            onNavigateToMenu = { navigatedToMenu = true },
            onNavigateToSignIn = { navigatedToSignIn = true },
            onNavigateToSignUp = { navigatedToSignUp = true },
            currentUser = "")

        composeRule.onNodeWithText(context.getString(R.string.sign_up)).performClick()
        assertFalse(navigatedToMenu)
        assertTrue(navigatedToSignUp)
        assertFalse(navigatedToSignIn)

    }


}