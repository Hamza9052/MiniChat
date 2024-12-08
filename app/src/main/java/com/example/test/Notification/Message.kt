package com.example.test.Notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.credentials.provider.Action
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.test.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.test.R
import com.example.test.UiHome.MessageScreen
import com.example.test.UiHome.Screen
import com.example.test.ViewModel.UserViewModel
import java.util.Random
import kotlin.getValue


class Message: FirebaseMessagingService() {

    private val channelID= "M-N"
    private val channelName = "MessageNotification"
    private val ViewModel = UserViewModel()
    private val notificationmanager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Handler(Looper.getMainLooper()).post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification()
        }
        val intent = Intent(applicationContext, MainActivity()::class.java).apply {
            // Pass any additional data or specify the route
            putExtra("route", "message/${message.data["title"]}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val build = NotificationCompat.Builder(applicationContext,channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentText(message.data["body"])
            .setContentTitle(message.data["title"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            with(NotificationManagerCompat.from(applicationContext)){
                if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                )!= PackageManager.PERMISSION_GRANTED
                )
                {
                    return@post
                }
                notify(Random().nextInt(3000),build.build())
            }
        }else{
            NotificationManagerCompat.from(applicationContext)
                .notify(Random().nextInt(3000),build.build())
        }

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