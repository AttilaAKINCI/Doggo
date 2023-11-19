package com.akinci.doggo.ui.feature.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.akinci.doggo.ui.features.dashboard.DashboardScreen
import com.akinci.doggo.ui.features.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@HiltAndroidTest
class DashboardScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DashboardScreen(
                onNavigateToDetail = { _, _ -> },
                animationCount = 1
            )
        }
    }

    @Test
    fun dashboard_check_content() {
        // welcome section is visible
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
        ).assertIsDisplayed()
    }
}