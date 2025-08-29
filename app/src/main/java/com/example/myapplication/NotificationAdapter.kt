package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Import NotificationItemType enum
import com.example.myapplication.NotificationItemType

class NotificationAdapter(private val notifications: List<NotificationItem>) : 
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivNotificationType: ImageView = view.findViewById(R.id.ivNotificationType)
        val tvNotificationTitle: TextView = view.findViewById(R.id.tvNotificationTitle)
        val tvTimeAgo: TextView = view.findViewById(R.id.tvTimeAgo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        
        holder.tvNotificationTitle.text = notification.title
        holder.tvTimeAgo.text = notification.timeAgo
        
        // Set icon based on notification type
        val iconResId = when(notification.type) {
            NotificationItemType.LESSON -> R.drawable.ic_notification_lesson
            NotificationItemType.QUIZ -> R.drawable.ic_notification_quiz
            NotificationItemType.PAYMENT -> R.drawable.ic_notification_payment
            NotificationItemType.SYSTEM -> R.drawable.ic_notification_system
        }
        holder.ivNotificationType.setImageResource(iconResId)
    }

    override fun getItemCount(): Int = notifications.size
}
