package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoNotifications: TextView
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Initialize notification manager
        notificationManager = NotificationManager.getInstance(this)

        // Set up back button
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Set up views
        recyclerView = findViewById(R.id.recyclerViewNotifications)
        tvNoNotifications = findViewById(R.id.tvNoNotifications)
        
        // Load and display notifications
        loadNotifications()
        
        // Mark all notifications as read
        notificationManager.markAllAsRead()
    }
    
    private fun loadNotifications() {
        val notifications = notificationManager.getAllNotifications()
        
        if (notifications.isEmpty()) {
            recyclerView.visibility = View.GONE
            tvNoNotifications.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvNoNotifications.visibility = View.GONE
            
            // Convert to NotificationItem list that the adapter expects
            val items = notifications.map { notification ->
                NotificationItem(
                    title = notification.title,
                    timeAgo = getTimeAgo(notification.timestamp),
                    type = convertNotificationType(notification.type)
                )
            }
            
            // Set up RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = NotificationAdapter(items)
        }
    }
    
    private fun getTimeAgo(date: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - date.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - date.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - date.time)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - date.time)
        
        return when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            days < 30 -> "$days days ago"
            else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }
    
    private fun convertNotificationType(type: NotificationType): NotificationItemType {
        return when (type) {
            NotificationType.LESSON -> NotificationItemType.LESSON
            NotificationType.QUIZ -> NotificationItemType.QUIZ
            NotificationType.PAYMENT -> NotificationItemType.PAYMENT
            NotificationType.SYSTEM -> NotificationItemType.SYSTEM
            NotificationType.COURSE_UPDATE -> NotificationItemType.LESSON
            NotificationType.NEW_COURSE -> NotificationItemType.LESSON
            NotificationType.PROMOTION -> NotificationItemType.SYSTEM
            NotificationType.ACHIEVEMENT -> NotificationItemType.SYSTEM
            NotificationType.REMINDER -> NotificationItemType.SYSTEM
        }
    }
}

// Enum for notification types
// Using NotificationItem and NotificationType from separate file
