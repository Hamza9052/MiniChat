package com.example.test.AccountManager

import android.app.Activity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import com.example.test.MainActivity
import android.content.Context

class accountManager(
    private val context: Context
) {
    private val credentialManager = CredentialManager.create(context)
    suspend fun login(email:String, password: String): Result{
        return try {
            credentialManager.createCredential(
                context = context,
                request = CreatePasswordRequest(
                    id = email,
                    password = password
                )
            )
            Result.Success(email)
        }catch (e: CreateCredentialCancellationException){
            e.printStackTrace()
            Result.Cancelled
        }catch (e: CreateCredentialException){
            e.printStackTrace()
            Result.Failure
        }
    }
}