package com.akinci.doggoappcompose.ui.main.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.akinci.doggoappcompose.ui.main.navigation.Navigation

private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED

@Composable
fun rememberDoggoAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    DoggoAppState(navController)
}

//  val navController = rememberNavController()
//  val backstackEntry = navController.currentBackStackEntryAsState()
//  val currentScreen = MainNavigation.fromRoute(backstackEntry.value?.destination?.route)


class DoggoAppState(
    val navController: NavHostController
) {
    fun navigateBack() { navController.popBackStack() }

    fun navigate(navigationRoute: Navigation, args: Map<String,String>? = null, from: NavBackStackEntry){
        if (from.lifecycleIsResumed()) {
            navController.navigate(navigationRoute.createRoute(args))
        }
    }
}