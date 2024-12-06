package com.example.test.Event

import com.example.test.Notification.Message

data class Loginstate (
    val loggedInUser: String? = null,
    var email:String = "",
    var password:String = "",
    val errorMessage: String? = null
    )