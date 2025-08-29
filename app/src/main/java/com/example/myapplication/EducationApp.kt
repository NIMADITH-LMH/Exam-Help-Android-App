package com.example.myapplication

import android.app.Application

/**
 * Application class for the Education App.
 * This is used to initialize components required by the application.
 */
class EducationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize notification channels
        NotificationUtils.createNotificationChannel(this)
    }
}
