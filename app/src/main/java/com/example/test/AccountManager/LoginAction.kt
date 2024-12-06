package com.example.test.AccountManager

sealed interface LoginAction {
    data class Logging(val result: ResultIn): LoginAction
    data class Login(val result: Result): LoginAction
    data class OnEmailChange(val email:String): LoginAction
    data class OnPasswordChange(val password:String): LoginAction
}