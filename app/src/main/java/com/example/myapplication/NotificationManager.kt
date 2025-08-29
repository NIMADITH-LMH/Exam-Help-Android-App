package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import java.util.UUID

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Date = Date(),
    val relatedItemId: String? = null,
    var isRead: Boolean = false
)

object NotificationManager {
    private const val PREF_NAME = "notification_preferences"
    private const val KEY_NOTIFICATIONS = "notifications"
    
    // Singleton instance
    private var instance: NotificationManager? = null
    
    // Get singleton instance
    fun getInstance(context: Context): NotificationManager {
        if (instance == null) {
            instance = NotificationManager
            instance!!.initialize(context.applicationContext)
        }
        return instance!!
    }
    
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    
    private fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    // Add a new notification
    fun addNotification(notification: Notification) {
        val notifications = getAllNotifications().toMutableList()
        notifications.add(0, notification) // Add to the beginning (newest first)
        saveNotifications(notifications)
    }
    
    // Get all notifications
    fun getAllNotifications(): List<Notification> {
        val notificationsJson = sharedPreferences.getString(KEY_NOTIFICATIONS, null) ?: return emptyList()
        val type = object : TypeToken<List<Notification>>() {}.type
        return gson.fromJson(notificationsJson, type) ?: emptyList()
    }
    
    // Save notifications
    private fun saveNotifications(notifications: List<Notification>) {
        val notificationsJson = gson.toJson(notifications)
        sharedPreferences.edit().putString(KEY_NOTIFICATIONS, notificationsJson).apply()
    }
    
    // Mark notification as read
    fun markAsRead(notificationId: String) {
        val notifications = getAllNotifications().toMutableList()
        val index = notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            notifications[index] = notifications[index].copy(isRead = true)
            saveNotifications(notifications)
        }
    }
    
    // Mark all notifications as read
    fun markAllAsRead() {
        val notifications = getAllNotifications().map { it.copy(isRead = true) }
        saveNotifications(notifications)
    }
    
    // Get unread notification count
    fun getUnreadCount(): Int {
        return getAllNotifications().count { !it.isRead }
    }
    
    // Delete a notification
    fun deleteNotification(notificationId: String) {
        val notifications = getAllNotifications().toMutableList()
        notifications.removeIf { it.id == notificationId }
        saveNotifications(notifications)
    }
    
    // Clear all notifications
    fun clearAllNotifications() {
        sharedPreferences.edit().remove(KEY_NOTIFICATIONS).apply()
    }
    
    // Create course update notification
    fun createCourseUpdateNotification(courseId: Int, courseName: String, updateMessage: String) {
        val notification = Notification(
            title = "Course Update",
            message = "$courseName: $updateMessage",
            type = NotificationType.COURSE_UPDATE,
            relatedItemId = courseId.toString()
        )
        addNotification(notification)
    }
    
    // Create new course notification
    fun createNewCourseNotification(courseId: Int, courseName: String) {
        val notification = Notification(
            title = "New Course Available",
            message = "Check out our new course: $courseName",
            type = NotificationType.NEW_COURSE,
            relatedItemId = courseId.toString()
        )
        addNotification(notification)
    }
    
    // Create achievement notification
    fun createAchievementNotification(achievementName: String) {
        val notification = Notification(
            title = "Achievement Unlocked",
            message = "Congratulations! You've earned: $achievementName",
            type = NotificationType.ACHIEVEMENT
        )
        addNotification(notification)
    }
    
    // Create reminder notification
    fun createReminderNotification(message: String) {
        val notification = Notification(
            title = "Reminder",
            message = message,
            type = NotificationType.REMINDER
        )
        addNotification(notification)
    }
}
