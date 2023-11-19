package com.akinci.doggoappcompose.ui.main.navigation

/**
 * For parametered navigation define create route function.
 * **/

sealed class Navigation(val route: String){
    object Splash: Navigation("splash")
    object Dashboard: Navigation("dashboard")
    object DetailWithBreed: Navigation("detail/{breed}") {
        override fun createRoute(args: Map<String, String>?): String {
            return route
                .replace("{breed}", args?.get("breed") ?: "")
        }
    }
    object DetailWithBreedAndSubBreed: Navigation("detail/{breed}/{subBreed}") {
        override fun createRoute(args: Map<String, String>?): String {
            return route
                .replace("{breed}", args?.get("breed") ?: "")
                .replace("{subBreed}", args?.get("subBreed") ?: "")
        }
    }

    open fun createRoute(args: Map<String, String>?): String{ return route }
}