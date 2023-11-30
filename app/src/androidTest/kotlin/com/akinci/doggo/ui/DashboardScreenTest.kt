package com.akinci.doggo.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.akinci.doggo.ui.features.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DashboardScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkBreedListDisplayed() = runTest {
        goToDashboardScreen()

        // Verify welcome message is displayed
        composeTestRule.onNodeWithTag("welcome_banner").assertIsDisplayed()

        // Verify Breed title is displayed
        composeTestRule.onNodeWithText("BREEDS").assertIsDisplayed()

        /* // welcome section is visible
         composeTestRule.onNodeWithText(
             "Welcome",
             substring = true,
             ignoreCase = true
         ).assertIsDisplayed()

         // breed list header is visible.
         composeTestRule.onNodeWithText(
             "BREEDS"
         ).assertIsDisplayed()

         composeTestRule.onNodeWithText("Buhund").performClick()

         // sub breed list header is visible.
         composeTestRule.onNodeWithText(
             "SUB BREEDS"
         ).assertIsDisplayed()

         //composeTestRule.onNodeWithText("Norwegian").performClick()

         // clicks floating action button
         composeTestRule.onNodeWithTag("floatingButton").performClick()

         // warning snackbar is visible for error
         composeTestRule.onNodeWithText(
             "Please choose",
             substring = true,
             ignoreCase = true
         ).assertIsDisplayed()*/
    }

    private fun goToDashboardScreen() {
        // Check lottie animation is displayed
        composeTestRule.onNodeWithTag("lottie_animation").assertIsDisplayed()

        // Wait until automatically navigated to Dashboard Screen
        composeTestRule.waitUntil(timeoutMillis = 3000L) {
            composeTestRule.onAllNodesWithText("BREEDS").fetchSemanticsNodes().isNotEmpty()
        }
    }
}