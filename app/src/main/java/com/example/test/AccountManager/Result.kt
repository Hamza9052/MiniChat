package com.example.test.AccountManager

sealed interface Result {
    data class Success(val name: String): Result
    data object Cancelled: Result
    data object Failure: Result
}

