package com.example.test.Event

import android.content.Context


interface UserEvent {
    data class Login(val email: String,val password: String,val state:(state:Boolean)->Unit) : UserEvent
    data class CreateAccount(val user: user,val state:(state:Boolean)->Unit) : UserEvent
    data class signOut(val state:(state:Boolean)->Unit) : UserEvent
    data class Upload_Image(var image:String) : UserEvent

}