package com.example.test.AccountManager

sealed interface ResultIn {
    data class Success(val name: String): ResultIn
    data object Cancelled: ResultIn
    data object Failure: ResultIn
    data object NoCredentials: ResultIn
}