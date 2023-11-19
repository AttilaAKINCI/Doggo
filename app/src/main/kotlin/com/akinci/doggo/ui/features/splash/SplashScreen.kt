package com.akinci.doggo.ui.features.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.doggo.R
import com.akinci.doggo.core.compose.UIModePreviews
import com.akinci.doggo.ui.ds.components.InfiniteLottieAnimation
import com.akinci.doggo.ui.ds.theme.DoggoTheme
import com.akinci.doggo.ui.features.NavGraphs
import com.akinci.doggo.ui.features.destinations.DashboardScreenDestination
import com.akinci.doggo.ui.features.splash.SplashViewContract.State
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    vm: SplashViewModel = hiltViewModel(),
) {
    val uiState: State by vm.stateFlow.collectAsStateWithLifecycle()

    if (uiState.isCompleted) {
         navigator.navigate(DashboardScreenDestination) {
             popUpTo(NavGraphs.root)
             launchSingleTop = true
         }
    }

    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            InfiniteLottieAnimation(animationId = R.raw.doggo)
        }
    }
}

@UIModePreviews
@Composable
fun SplashScreenContentPreview() {
    DoggoTheme {
        SplashScreenContent()
    }
}
