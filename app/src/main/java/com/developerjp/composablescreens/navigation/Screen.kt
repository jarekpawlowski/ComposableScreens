package com.developerjp.composablescreens.navigation

sealed class Screen(val route: String){
    object MainScreen : Screen("MainScreen")
    object DetailScreen: Screen( "DetailScreen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
