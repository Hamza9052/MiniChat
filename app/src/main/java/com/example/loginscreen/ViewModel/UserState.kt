package com.example.loginscreen.ViewModel

data class UserState(
    val loginId:String,
    val fullName:String,
    val phoneNumber:String="",
    val Email:String="",
    val password:String ="",
)
