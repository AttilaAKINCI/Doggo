package com.akinci.doggo.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import com.akinci.doggo.ui.features.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@HiltAndroidTest
class DetailScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            /*DetailScreen(
                args = DetailViewContract("hound", "afghan"),
                onBackPress = { }
            )*/
        }
    }

    @Test
    fun detail_check_content() = runTest {
        goToDetailScreen()

        /* // doggo image list header is visible.
        composeTestRule.onNodeWithText(
            "hound / afghan",
            substring = true,
            ignoreCase = true
        ).assertIsDisplayed()*/
    }

    private fun goToDetailScreen() {
        // Check lottie animation is displayed
        composeTestRule.onNodeWithTag("lottie_animation").assertIsDisplayed()

        // Wait until automatically navigated to Dashboard Screen
        composeTestRule.waitUntil(timeoutMillis = 3000L) {
            composeTestRule.onAllNodesWithText("BREEDS").fetchSemanticsNodes().isNotEmpty()
        }
    }
}