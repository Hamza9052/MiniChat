package com.example.test.AccountManager

import android.app.Activity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import com.example.test.MainActivity
import android.content.Context
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.navigation.NavController
import com.example.test.Event.Loginstate
import com.example.test.Event.UserEvent
import com.example.test.UiHome.Screen
import com.example.test.ViewModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage

class accountManager(
    private val context: Context,
    private val VM: UserViewModel,
    private var state:Loginstate
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

    suspend fun Logging(): ResultIn{
        return try {
            val credential = credentialManager.getCredential(
                context = context,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )

            val credentialResponse = credential.credential as? PasswordCredential
            ?: return ResultIn.Failure
            state.email = credentialResponse.id
            state.password = credentialResponse.password



            ResultIn.Success(credentialResponse.id)
        }catch (e: GetCredentialCancellationException){
            e.printStackTrace()
            ResultIn.Cancelled
        }catch (e: NoCredentialException){
            e.printStackTrace()
            ResultIn.NoCredentials
        }catch (e: GetCredentialException){
            e.printStackTrace()
            ResultIn.Failure
        }

    }


}