package com.example.myapplication

data class Course(
    val id: Int = 0,
    val title: String,
    val instructor: String = "",
    val rating: Float,
    val numReviews: Int = 0,
    val price: String = "",
    val imageUrl: Int = 0,
    val progress: Int = 0,
    val category: String = "",
    val lessonsCount: Int = 0,
    val imageResId: Int = 0,
    val description: String = "This course provides comprehensive knowledge on the subject with interactive lessons and hands-on exercises.",
    val lessons: List<Lesson> = emptyList(),
    val quizzes: List<Quiz> = emptyList(),
    val level: String = "Beginner" // Beginner, Intermediate, Advanced
)
