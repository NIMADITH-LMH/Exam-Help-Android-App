package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ClassSelectionActivity : AppCompatActivity() {
    
    private lateinit var userManager: UserManager
    private var selectedLevel: UserManager.UserLevel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize user manager
        userManager = UserManager.getInstance(this)
        
        // Check if user is logged in
        if (userManager.getCurrentUser() == null) {
            // Redirect to sign in activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        
        setContentView(R.layout.activity_class_selection)

        val btnElementary = findViewById<Button>(R.id.btnElementarySchool)
        val btnJuniorHigh = findViewById<Button>(R.id.btnJuniorHighSchool)
        val btnSeniorHigh = findViewById<Button>(R.id.btnSeniorHighSchool)
        val btnCollege = findViewById<Button>(R.id.btnCollegeStudent)
        val btnNext = findViewById<Button>(R.id.btnNext)

        // Set onClick listeners for all class options
        btnElementary.setOnClickListener {
            selectClassOption(it as Button)
        }

        btnJuniorHigh.setOnClickListener {
            selectClassOption(it as Button)
        }

        btnSeniorHigh.setOnClickListener {
            selectClassOption(it as Button)
        }

        btnCollege.setOnClickListener {
            selectClassOption(it as Button)
        }

        // Next button functionality
        btnNext.setOnClickListener {
            // Update user's level if one is selected
            selectedLevel?.let { level ->
                val currentUser = userManager.getCurrentUser()
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(level = level)
                    userManager.updateUserProfile(updatedUser)
                }
            }
            
            // Go to course detail screen
            startActivity(Intent(this, CourseDetailActivity::class.java))
        }
    }

    private fun selectClassOption(selectedButton: Button) {
        // Reset all buttons
        val buttons = listOf(
            findViewById<Button>(R.id.btnElementarySchool),
            findViewById<Button>(R.id.btnJuniorHighSchool),
            findViewById<Button>(R.id.btnSeniorHighSchool),
            findViewById<Button>(R.id.btnCollegeStudent)
        )

        // Reset text color for all buttons
        for (button in buttons) {
            button.setTextColor(resources.getColor(R.color.text_primary, null))
        }

        // Set selected button's text color
        selectedButton.setTextColor(resources.getColor(R.color.primary_blue, null))
        
        // Set the selected level
        selectedLevel = when(selectedButton.id) {
            R.id.btnElementarySchool -> UserManager.UserLevel.ELEMENTARY
            R.id.btnJuniorHighSchool -> UserManager.UserLevel.JUNIOR
            R.id.btnSeniorHighSchool -> UserManager.UserLevel.HIGH_SCHOOL
            R.id.btnCollegeStudent -> UserManager.UserLevel.COLLEGE
            else -> UserManager.UserLevel.ELEMENTARY
        }
    }
}
