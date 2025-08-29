package com.example.myapplication

import java.util.Date
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    var username: String,
    var email: String,
    var password: String,
    var fullName: String = "",
    var profileImageUrl: String = "",
    var level: UserManager.UserLevel = UserManager.UserLevel.ELEMENTARY,
    var bio: String = "",
    var dateJoined: Date = Date(),
    var notificationsEnabled: Boolean = true,
    var enrolledCourses: MutableList<Int> = mutableListOf(),
    var completedLessons: MutableList<String> = mutableListOf(),
    var completedQuizzes: MutableList<String> = mutableListOf()
)
