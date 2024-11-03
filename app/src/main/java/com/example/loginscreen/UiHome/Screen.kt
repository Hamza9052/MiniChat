package com.example.loginscreen.UiHome

sealed class Screen(val route:String) {
    object Login:Screen("login")
    object register:Screen("register")
    object Main_Screen:Screen("main_screen")
    object Message:Screen("Message")
}