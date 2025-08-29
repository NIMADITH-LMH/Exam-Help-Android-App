package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var etUsernameEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var tvForgotPassword: TextView
    
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        
        userManager = UserManager.getInstance(this)
        
        // Check if user is already logged in
        if (userManager.getCurrentUser() != null) {
            navigateToHome()
            return
        }
        
        // Initialize views
        etUsernameEmail = findViewById(R.id.etUsernameEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        
        // Set up sign in button
        btnSignIn.setOnClickListener {
            signIn()
        }
        
        // Set up sign up text click
        tvSignUp.setOnClickListener {
            // Navigate to sign up activity
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        
        // Set up forgot password text click
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password functionality coming soon", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun signIn() {
        val usernameEmail = etUsernameEmail.text.toString().trim()
        val password = etPassword.text.toString()
        
        if (usernameEmail.isEmpty()) {
            etUsernameEmail.error = "Please enter your username or email"
            return
        }
        
        if (password.isEmpty()) {
            etPassword.error = "Please enter your password"
            return
        }
        
        val user = userManager.loginUser(usernameEmail, password)
        if (user != null) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            navigateToHome()
        } else {
            Toast.makeText(this, "Invalid username/email or password", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
