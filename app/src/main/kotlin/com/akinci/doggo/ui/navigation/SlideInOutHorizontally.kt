package com.akinci.doggo.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

object SlideInOutHorizontally : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition =
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(),
        )

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition =
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(),
        )

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition =
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(),
        )

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition =
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(),
        )
}