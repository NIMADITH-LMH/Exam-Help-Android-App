package com.example.myapplication

enum class NotificationItemType {
    LESSON,
    QUIZ,
    PAYMENT,
    SYSTEM
}

data class NotificationItem(
    val title: String,
    val timeAgo: String,
    val type: NotificationItemType
)
