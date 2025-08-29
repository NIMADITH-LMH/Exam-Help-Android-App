package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Add notification button functionality
        val btnNotification = findViewById<ImageButton>(id.btnNotification)
        btnNotification?.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        
        // Add profile button functionality (if exists in layout)
        val btnProfile = findViewById<ImageButton>(id.btnProfile)
        btnProfile?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}