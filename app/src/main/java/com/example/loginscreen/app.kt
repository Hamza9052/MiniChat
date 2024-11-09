package com.hamza.test

import android.app.Application
import com.google.firebase.FirebaseApp

class app:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}