package com.example.myapplication

data class Quiz(
    val id: Int,
    val title: String,
    val questions: List<QuizQuestion>,
    val timeLimit: Int // in minutes
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int
)
