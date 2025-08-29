package com.example.myapplication

data class Review(
    val id: Int,
    val username: String,
    val rating: Float,
    val content: String,
    val date: String,
    val userProfileImageResId: Int = 0
)
