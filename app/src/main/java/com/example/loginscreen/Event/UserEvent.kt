package com.example.loginscreen.Event

interface UserEvent {
    object SaveContact : UserEvent
    data class SetloginId(val loginId: String) : UserEvent
    data class SetfullName(val fullName: String) : UserEvent
    data class SetphoneNumber(val phoneNumber: String) : UserEvent
    data class SetEmail(val Email: String) : UserEvent
    data class Setpassword(val password: String) : UserEvent
    data class SetCorrectpassword(val Correctpassword: String) : UserEvent

}