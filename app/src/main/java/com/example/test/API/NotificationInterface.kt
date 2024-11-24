package com.example.test.API


import android.util.Log
import retrofit2.Call
import com.example.test.Notification.NotificationIn
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {
    @POST("/v1/projects/test-6d005/messages:send")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    fun sendNotification(
        @Body message:NotificationIn,
        @Header("Authorization") accessToken:String
    ): Call<NotificationIn>


}

