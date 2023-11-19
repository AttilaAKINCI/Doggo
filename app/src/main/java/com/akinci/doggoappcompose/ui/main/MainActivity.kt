package com.akinci.doggoappcompose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.akinci.doggoappcompose.ui.feaute.dashboard.view.DashboardScreen
import com.akinci.doggoappcompose.ui.feaute.detail.view.DetailScreen
import com.akinci.doggoappcompose.ui.feaute.detail.view.DetailScreenArgs
import com.akinci.doggoappcompose.ui.feaute.splash.view.SplashScreen
import com.akinci.doggoappcompose.ui.main.navigation.Navigation
import com.akinci.doggoappcompose.ui.main.util.DoggoAppState
import com.akinci.doggoappcompose.ui.main.util.rememberDoggoAppState
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoggoApp()
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun DoggoApp(
    appState: DoggoAppState = rememberDoggoAppState()
){
    DoggoAppComposeTheme {
        MainNavHost(appState)
    }
}

@ExperimentalAnimationApi
@Composable
fun MainNavHost(
    appState: DoggoAppState,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = appState.navController,
        startDestination = Navigation.Splash.route,
        modifier = modifier
    ){
        composable(route = Navigation.Splash.route){
            SplashScreen(onTimeout = { appState.navigate(Navigation.Dashboard, from = it) })
        }
        composable(route = Navigation.Dashboard.route){
            DashboardScreen(onNavigateToDetail = { selectedBreed, selectedSubBreed ->
                if(selectedSubBreed.isNotBlank()){
                    appState.navigate(
                        navigationRoute = Navigation.DetailWithBreedAndSubBreed,
                        args = mapOf(
                            "breed" to selectedBreed,
                            "subBreed" to selectedSubBreed
                        ),
                        from = it
                    )
                }else{
                    appState.navigate(
                        navigationRoute = Navigation.DetailWithBreed,
                        args = mapOf("breed" to selectedBreed),
                        from = it
                    )
                }
            })
        }
        composable(
            route = Navigation.DetailWithBreedAndSubBreed.route,
            arguments = listOf(
                navArgument("breed") { type = NavType.StringType },
                navArgument("subBreed") { type = NavType.StringType },
            )
        ){ entry ->
            DetailScreen(
                args = DetailScreenArgs(
                    breedName = entry.arguments?.getString("breed") ?: "",
                    subBreedName = entry.arguments?.getString("subBreed") ?: "",
                ),
                onBackPress = { appState.navigateBack() })
        }
        composable(
            route = Navigation.DetailWithBreed.route,
            arguments = listOf(navArgument("breed") { type = NavType.StringType })
        ){ entry ->
            DetailScreen(
                args = DetailScreenArgs(breedName = entry.arguments?.getString("breed") ?: ""),
                onBackPress = { appState.navigateBack() })
        }
    }
}