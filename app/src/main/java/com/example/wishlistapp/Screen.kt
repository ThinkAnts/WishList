package com.example.wishlistapp

sealed class Screen(val route: String) {
    object Homescreen: Screen(route = "home_screen")
    object AddScreen: Screen(route = "add_screen")
}