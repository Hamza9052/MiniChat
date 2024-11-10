package com.hamza.test.Event

import android.content.Context


interface UserEvent {
    data class Login(val user: user,val state:(state:Boolean)->Unit) : UserEvent
    data class CreateAccount(val user: user,val state:(state:Boolean)->Unit) : UserEvent
    data class signOut(val user: user,val state:(state:Boolean)->Unit) : UserEvent


}