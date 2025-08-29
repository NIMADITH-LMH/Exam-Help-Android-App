package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etFullName: EditText
    private lateinit var etBio: EditText
    private lateinit var spinnerLevel: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageButton
    
    private lateinit var userManager: UserManager
    private var selectedLevel = UserManager.UserLevel.ELEMENTARY
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        
        userManager = UserManager.getInstance(this)
        
        // Get current user or redirect to login
        currentUser = userManager.getCurrentUser() ?: run {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        
        // Initialize views
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etFullName = findViewById(R.id.etFullName)
        etBio = findViewById(R.id.etBio)
        spinnerLevel = findViewById(R.id.spinnerLevel)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)
        
        // Set up level spinner
        setupLevelSpinner()
        
        // Set current user data
        populateUserData()
        
        // Set up button clicks
        btnSave.setOnClickListener {
            if (validateInputs()) {
                saveUserProfile()
            }
        }
        
        btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupLevelSpinner() {
        val levels = UserManager.UserLevel.values().map { 
            when(it) {
                UserManager.UserLevel.ELEMENTARY -> "Elementary School"
                UserManager.UserLevel.JUNIOR -> "Junior High School"
                UserManager.UserLevel.HIGH_SCHOOL -> "High School"
                UserManager.UserLevel.COLLEGE -> "College"
                UserManager.UserLevel.PROFESSIONAL -> "Professional"
            }
        }
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, levels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLevel.adapter = adapter
        
        // Set selected level
        spinnerLevel.setSelection(currentUser.level.ordinal)
        
        spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLevel = UserManager.UserLevel.values()[position]
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLevel = currentUser.level
            }
        }
    }
    
    private fun populateUserData() {
        etUsername.setText(currentUser.username)
        etEmail.setText(currentUser.email)
        etFullName.setText(currentUser.fullName)
        etBio.setText(currentUser.bio)
    }
    
    private fun validateInputs(): Boolean {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val fullName = etFullName.text.toString().trim()
        
        // Validate username
        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            return false
        }
        
        if (username.length < 4) {
            etUsername.error = "Username must be at least 4 characters long"
            return false
        }
        
        if (username != currentUser.username && !userManager.isUsernameAvailable(username)) {
            etUsername.error = "Username is already taken"
            return false
        }
        
        // Validate email
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Please enter a valid email address"
            return false
        }
        
        if (email != currentUser.email && !userManager.isEmailAvailable(email)) {
            etEmail.error = "Email is already registered"
            return false
        }
        
        // Validate full name
        if (fullName.isEmpty()) {
            etFullName.error = "Full name is required"
            return false
        }
        
        return true
    }
    
    private fun saveUserProfile() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val fullName = etFullName.text.toString().trim()
        val bio = etBio.text.toString().trim()
        
        // Create updated user
        val updatedUser = currentUser.copy(
            username = username,
            email = email,
            fullName = fullName,
            bio = bio,
            level = selectedLevel
        )
        
        if (userManager.updateUserProfile(updatedUser)) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            
            // Return to profile activity
            finish()
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }
}
