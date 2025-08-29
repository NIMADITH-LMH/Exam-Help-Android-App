package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply the splash theme
        setContentView(R.layout.activity_splash)
        
        // Hide system UI for immersive experience
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        
        // Get references to views
        val logoImageView = findViewById<ImageView>(R.id.ivLogo)
        val appNameTextView = findViewById<TextView>(R.id.tvAppName)
        val taglineTextView = findViewById<TextView>(R.id.tvTagline)
        
        // Set initial states for animation
        logoImageView.alpha = 0f
        logoImageView.scaleX = 0.3f
        logoImageView.scaleY = 0.3f
        
        appNameTextView.alpha = 0f
        taglineTextView.alpha = 0f
        
        // Animate logo
        val logoAnimSet = AnimatorSet()
        logoAnimSet.playTogether(
            ObjectAnimator.ofFloat(logoImageView, "alpha", 0f, 1f),
            ObjectAnimator.ofFloat(logoImageView, "scaleX", 0.3f, 1f),
            ObjectAnimator.ofFloat(logoImageView, "scaleY", 0.3f, 1f)
        )
        logoAnimSet.duration = 1000
        logoAnimSet.interpolator = BounceInterpolator()
        logoAnimSet.start()
        
        // Animate app name with delay
        Handler(Looper.getMainLooper()).postDelayed({
            appNameTextView.animate()
                .alpha(1f)
                .setDuration(800)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
            
            // Animate tagline with further delay
            Handler(Looper.getMainLooper()).postDelayed({
                taglineTextView.animate()
                    .alpha(1f)
                    .setDuration(800)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }, 300)
        }, 500)
        
        // Navigate to main screen after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }, 3000)
    }
}
