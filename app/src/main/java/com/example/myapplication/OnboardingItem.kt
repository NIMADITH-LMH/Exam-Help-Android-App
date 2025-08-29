package com.example.myapplication

data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: Int,
    val imageResId: Int = 0
) : java.io.Serializable
