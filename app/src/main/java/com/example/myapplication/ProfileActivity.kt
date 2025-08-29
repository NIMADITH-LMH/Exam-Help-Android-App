package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var userManager: UserManager
    private lateinit var notificationManager: NotificationManager
    private var unreadNotifications = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        userManager = UserManager.getInstance(this)
        notificationManager = NotificationManager.getInstance(this)
        
        // Check if user is logged in
        if (userManager.getCurrentUser() == null) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        
        // Set user data
        setupUserData()
        
        // Set up button clicks
        setupButtonClicks()
        
        // Set up navigation
        setupNavigation()
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh user data in case it was updated
        setupUserData()
        
        // Update notification badge
        updateNotificationBadge()
    }
    
    private fun setupUserData() {
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val ivProfileImage = findViewById<ImageView>(R.id.ivProfileImage)
        val tvLevel = findViewById<TextView>(R.id.tvLevel)
        
        val currentUser = userManager.getCurrentUser() ?: return
        
        tvUsername.text = currentUser.username
        tvEmail.text = currentUser.email
        
        // Set educational level
        val levelText = when(currentUser.level) {
            UserManager.UserLevel.ELEMENTARY -> "Elementary School"
            UserManager.UserLevel.JUNIOR -> "Junior High School"
            UserManager.UserLevel.HIGH_SCHOOL -> "High School"
            UserManager.UserLevel.COLLEGE -> "College"
            UserManager.UserLevel.PROFESSIONAL -> "Professional"
        }
        tvLevel.text = levelText
        
        // For now, use a default image
        ivProfileImage.setImageResource(R.drawable.img_profile)
    }
    
    private fun updateNotificationBadge() {
        val tvNotificationBadge = findViewById<TextView>(R.id.tvNotificationBadge)
        unreadNotifications = notificationManager.getUnreadCount()
        
        if (unreadNotifications > 0) {
            tvNotificationBadge.visibility = android.view.View.VISIBLE
            tvNotificationBadge.text = if (unreadNotifications > 99) "99+" else unreadNotifications.toString()
        } else {
            tvNotificationBadge.visibility = android.view.View.GONE
        }
    }
    
    private fun setupButtonClicks() {
        val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
        val btnMyLearning = findViewById<TextView>(R.id.btnMyLearning)
        val btnNotifications = findViewById<TextView>(R.id.btnNotifications)
        val btnPaymentHistory = findViewById<TextView>(R.id.btnPaymentHistory)
        val btnSettings = findViewById<TextView>(R.id.btnSettings)
        val btnHelp = findViewById<TextView>(R.id.btnHelp)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        
        btnEditProfile.setOnClickListener {
            // Start edit profile activity
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        
        btnMyLearning.setOnClickListener {
            startActivity(Intent(this, MyCoursesActivity::class.java))
        }
        
        btnNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        
        btnPaymentHistory.setOnClickListener {
            startActivity(Intent(this, PaymentHistoryActivity::class.java))
        }
        
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        btnHelp.setOnClickListener {
            startActivity(Intent(this, HelpCenterActivity::class.java))
        }
        
        btnLogout.setOnClickListener {
            // Perform logout
            userManager.logout()
            
            // Return to sign in screen
            startActivity(Intent(this, SignInActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }
    }
    
    private fun setupNavigation() {
        val btnHome = findViewById<ImageView>(R.id.btnNavHome)
        val btnSearch = findViewById<ImageView>(R.id.btnNavSearch)
        val btnCourses = findViewById<ImageView>(R.id.btnNavCourses)
        val btnProfile = findViewById<ImageView>(R.id.btnNavProfile)
        
        // Set listeners for navigation buttons
        btnHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        
        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }
        
        btnCourses.setOnClickListener {
            startActivity(Intent(this, MyCoursesActivity::class.java))
            finish()
        }
        
        btnProfile.setOnClickListener {
            // Already on profile
        }
    }
}
