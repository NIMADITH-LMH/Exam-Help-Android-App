package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etFullName: EditText
    private lateinit var spinnerLevel: Spinner
    private lateinit var btnSignUp: Button
    private lateinit var tvLogin: TextView
    
    private lateinit var userManager: UserManager
    private var selectedLevel = UserManager.UserLevel.ELEMENTARY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        
        userManager = UserManager.getInstance(this)
        
        // Initialize views
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etFullName = findViewById(R.id.etFullName)
        spinnerLevel = findViewById(R.id.spinnerLevel)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLogin = findViewById(R.id.tvLogin)
        
        // Set up user level spinner
        setupLevelSpinner()
        
        // Set up sign up button
        btnSignUp.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }
        
        // Set up login text click
        tvLogin.setOnClickListener {
            // Navigate to login activity
            startActivity(Intent(this, SignInActivity::class.java))
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
        
        spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLevel = UserManager.UserLevel.values()[position]
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLevel = UserManager.UserLevel.ELEMENTARY
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
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
        
        if (!userManager.isUsernameAvailable(username)) {
            etUsername.error = "Username is already taken"
            return false
        }
        
        // Validate email
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            return false
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Please enter a valid email address"
            return false
        }
        
        if (!userManager.isEmailAvailable(email)) {
            etEmail.error = "Email is already registered"
            return false
        }
        
        // Validate password
        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            return false
        }
        
        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters long"
            return false
        }
        
        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            return false
        }
        
        // Validate full name
        if (fullName.isEmpty()) {
            etFullName.error = "Full name is required"
            return false
        }
        
        return true
    }
    
    private fun registerUser() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val fullName = etFullName.text.toString().trim()
        
        val user = User(
            username = username,
            email = email,
            password = password,
            fullName = fullName,
            level = selectedLevel
        )
        
        if (userManager.registerUser(user)) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
            
            // Auto login after registration
            userManager.loginUser(username, password)
            
            // Create welcome notification
            val notificationManager = NotificationManager.getInstance(this)
            notificationManager.addNotification(
                Notification(
                    title = "Welcome to E-Learning",
                    message = "Thank you for registering! Start exploring our courses.",
                    type = NotificationType.SYSTEM
                )
            )
            
            // Navigate to home activity
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
        }
    }
}
