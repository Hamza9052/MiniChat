package com.example.test.UiHome

sealed class Screen(val route:String) {
    object Login:Screen("login")
    object register:Screen("register")
    object Main:Screen("main_screen")
    object Message:Screen("Message")
    object Profile:Screen("profile")
}