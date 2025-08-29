package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        setupSettings()
        setupToolbar()
    }
    
    private fun setupToolbar() {
        // Setup back button
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupSettings() {
        // Privacy Policy
        findViewById<TextView>(R.id.btnPrivacyPolicy).setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        // Terms & Conditions
        findViewById<TextView>(R.id.btnTerms).setOnClickListener {
            startActivity(Intent(this, TermsAndConditionsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
