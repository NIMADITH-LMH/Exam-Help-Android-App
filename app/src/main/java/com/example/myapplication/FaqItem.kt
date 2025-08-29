package com.example.myapplication

data class FaqItem(
    val question: String, 
    val answer: String,
    var isExpanded: Boolean = false
)
