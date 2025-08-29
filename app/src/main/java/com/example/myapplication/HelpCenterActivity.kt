package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HelpCenterActivity : AppCompatActivity() {
    
    private lateinit var rvFaq: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)
        
        setupToolbar()
        setupFaqList()
    }
    
    private fun setupToolbar() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }
    
    private fun setupFaqList() {
        rvFaq = findViewById(R.id.rvFaq)
        
        val faqItems = listOf(
            FaqItem(
                "How do I enroll in a course?",
                "To enroll in a course, navigate to the course detail page and click on the 'Enroll' button. " +
                        "You may need to complete the payment process if it's a paid course."
            ),
            FaqItem(
                "How do I track my progress?",
                "Your course progress is automatically tracked as you complete lessons and quizzes. " +
                        "You can view your progress on the course page or in the 'My Learning' section."
            ),
            FaqItem(
                "Can I download courses for offline viewing?",
                "Yes, most courses allow you to download videos for offline viewing. " +
                        "Look for the download icon on the video player when watching a lesson."
            ),
            FaqItem(
                "How do I get a certificate?",
                "Certificates are automatically generated once you complete all required lessons and assessments in a course. " +
                        "You can access your certificates from the 'My Learning' section."
            ),
            FaqItem(
                "What is the refund policy?",
                "We offer a 30-day money-back guarantee for most courses. If you're not satisfied with a course, " +
                        "you can request a refund within 30 days of purchase through the 'Payment History' section."
            ),
            FaqItem(
                "How do I change my email or password?",
                "You can update your email address and password in the Settings page. " +
                        "Go to your Profile, tap on Settings, and then select 'Change Password'."
            )
        )
        
        rvFaq.layoutManager = LinearLayoutManager(this)
        rvFaq.adapter = FaqAdapter(faqItems)
    }
}

// Using FaqItem from separate file