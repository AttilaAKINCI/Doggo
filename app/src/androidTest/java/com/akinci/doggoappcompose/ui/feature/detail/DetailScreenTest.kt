package com.akinci.doggoappcompose.ui.feature.detail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.akinci.doggoappcompose.ui.feaute.detail.view.DetailScreen
import com.akinci.doggoappcompose.ui.feaute.detail.view.DetailScreenArgs
import com.akinci.doggoappcompose.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
            DetailScreen(
                args = DetailScreenArgs("hound", "afghan"),
                onBackPress = { }
            )
        }
    }

    @Test
    fun detail_check_content() {
        // doggo image list header is visible.
        composeTestRule.onNodeWithText(
            "hound / afghan",
            substring = true,
            ignoreCase = true
        ).assertIsDisplayed()
    }
}