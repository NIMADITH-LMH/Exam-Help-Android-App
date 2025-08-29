package com.example.myapplication

data class Lesson(
    val id: Int,
    val title: String,
    val duration: String,
    val isCompleted: Boolean = false
)
