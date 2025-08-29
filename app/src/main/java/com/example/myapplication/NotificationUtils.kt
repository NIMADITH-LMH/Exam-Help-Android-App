package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * Utility class for handling notifications across the app.
 */
object NotificationUtils {

    private const val CHANNEL_ID = "education_app_channel"
    private const val CHANNEL_NAME = "Education App"
    private const val CHANNEL_DESCRIPTION = "Notifications from Education App"
    
    private const val NOTIFICATION_ID_ENROLLMENT = 1001
    private const val NOTIFICATION_ID_PAYMENT = 1002
    private const val NOTIFICATION_ID_COURSE_REMINDER = 1003
    private const val NOTIFICATION_ID_QUIZ = 1004
    
    /**
     * Creates the notification channel (required for Android 8.0+)
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Show notification for successful enrollment
     */
    fun showEnrollmentSuccessNotification(context: Context, courseTitle: String) {
        val intent = Intent(context, MyCoursesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Successfully Enrolled!")
            .setContentText("You've successfully enrolled in $courseTitle")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("You've successfully enrolled in the course: $courseTitle. You can now access all course materials."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        // Show notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_ENROLLMENT, builder.build())
    }
    
    /**
     * Show notification for successful payment
     */
    fun showPaymentSuccessNotification(context: Context, amount: String, courseTitle: String) {
        val intent = Intent(context, MyCoursesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Payment Successful")
            .setContentText("Your payment of $amount for $courseTitle was successful")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your payment of $amount for the course: $courseTitle was successful. You now have full access to the course content."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        // Show notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_PAYMENT, builder.build())
    }
    
    /**
     * Show notification for course reminder
     */
    fun showCourseReminderNotification(context: Context, courseTitle: String) {
        val intent = Intent(context, MyCoursesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Course Reminder")
            .setContentText("Continue your progress in $courseTitle")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        // Show notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_COURSE_REMINDER, builder.build())
    }
    
    /**
     * Show notification for quiz availability
     */
    fun showQuizAvailableNotification(context: Context, courseTitle: String, quizTitle: String) {
        val intent = Intent(context, MyCoursesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New Quiz Available")
            .setContentText("Quiz '$quizTitle' is now available in $courseTitle")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        // Show notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_QUIZ, builder.build())
    }
}
