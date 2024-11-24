package com.example.test.Notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.credentials.provider.Action
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.test.R
import java.util.Random


class Message: FirebaseMessagingService() {

    private val channelID= "M-N"
    private val channelName = "MessageNotification"

    private val notificationmanager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification()
        }

        val build = NotificationCompat.Builder(applicationContext,channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentText(message.data["body"])
            .setContentTitle(message.data["title"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            with(NotificationManagerCompat.from(applicationContext)){
                if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                )!= PackageManager.PERMISSION_GRANTED
                )
                {
                    return
                }
                notify(Random().nextInt(3000),build.build())
            }
        }else{
            NotificationManagerCompat.from(applicationContext)
                .notify(Random().nextInt(3000),build.build())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(){
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationmanager.createNotificationChannel(channel)
    }
}