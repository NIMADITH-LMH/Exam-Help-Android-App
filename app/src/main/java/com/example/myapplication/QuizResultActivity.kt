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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class QuizResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        
        android.util.Log.d("QuizResultActivity", "onCreate started")

        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)
        val courseName = intent.getStringExtra("COURSE_NAME") ?: "Course"
        
        // Get references to views
        val resultCardView = findViewById<CardView>(R.id.cardViewResult)
        val resultImage = findViewById<ImageView>(R.id.imageViewResult) 
        val scoreTextView = findViewById<TextView>(R.id.textViewScore)
        val percentageTextView = findViewById<TextView>(R.id.textViewPercentage)
        val resultMessageTextView = findViewById<TextView>(R.id.textViewResultMessage)
        val btnCertificate = findViewById<Button>(R.id.btnCertificate)
        val btnContinue = findViewById<Button>(R.id.btnContinue)
        val btnTryAgain = findViewById<Button>(R.id.btnTryAgain)
        
        // Set initial states for animations
        resultCardView.alpha = 0f
        resultCardView.scaleX = 0.8f
        resultCardView.scaleY = 0.8f
        
        scoreTextView.alpha = 0f
        percentageTextView.alpha = 0f
        resultMessageTextView.alpha = 0f
        btnCertificate.alpha = 0f
        btnContinue.alpha = 0f
        btnTryAgain.alpha = 0f
        
        // Display score
        scoreTextView.text = "$score / $total"
        
        // Calculate percentage
        val percentage = (score.toFloat() / total.toFloat()) * 100
        percentageTextView.text = "${percentage.toInt()}%"
        
        // Set result message and image based on score
        val resultMessage = when {
            percentage >= 80 -> "Excellent!"
            percentage >= 60 -> "Good job!"
            percentage >= 40 -> "Keep practicing!"
            else -> "Try again!"
        }
        resultMessageTextView.text = resultMessage
        
        // Set appropriate result image
        resultImage.setImageResource(when {
            percentage >= 80 -> R.drawable.quiz_result_excellent
            percentage >= 60 -> R.drawable.quiz_result_good
            percentage >= 40 -> R.drawable.quiz_result_average
            else -> R.drawable.quiz_result_poor
        })
        
        // Certificate button - only visible for scores >= 70%
        if (percentage >= 70) {
            btnCertificate.visibility = View.VISIBLE
            btnCertificate.setOnClickListener {
                val intent = Intent(this, CertificateActivity::class.java).apply {
                    putExtra("userName", "Student") // Replace with actual user name
                    putExtra("courseName", courseName)
                    putExtra("score", score)
                    putExtra("maxScore", total)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        } else {
            btnCertificate.visibility = View.GONE
        }
        
        // Continue button - leads to payment screen
        btnContinue.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        // Try again button
        btnTryAgain.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("COURSE_ID", getIntent().getIntExtra("COURSE_ID", 0))
            intent.putExtra("QUIZ_ID", getIntent().getIntExtra("QUIZ_ID", 0))
            intent.putExtra("COURSE_NAME", courseName)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
        
        // Start animations with staggered timing
        Handler(Looper.getMainLooper()).postDelayed({
            // Card animation
            val cardAnimSet = AnimatorSet()
            cardAnimSet.playTogether(
                ObjectAnimator.ofFloat(resultCardView, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(resultCardView, "scaleX", 0.8f, 1f),
                ObjectAnimator.ofFloat(resultCardView, "scaleY", 0.8f, 1f)
            )
            cardAnimSet.duration = 800
            cardAnimSet.interpolator = AccelerateDecelerateInterpolator()
            cardAnimSet.start()
            
            // Animate score with delay
            Handler(Looper.getMainLooper()).postDelayed({
                scoreTextView.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .start()
                
                // Animate percentage with bounce
                Handler(Looper.getMainLooper()).postDelayed({
                    percentageTextView.alpha = 1f
                    ObjectAnimator.ofFloat(percentageTextView, "scaleX", 0.5f, 1.2f, 1f).apply {
                        duration = 800
                        interpolator = BounceInterpolator()
                        start()
                    }
                    ObjectAnimator.ofFloat(percentageTextView, "scaleY", 0.5f, 1.2f, 1f).apply {
                        duration = 800
                        interpolator = BounceInterpolator()
                        start()
                    }
                    
                    // Animate message
                    Handler(Looper.getMainLooper()).postDelayed({
                        resultMessageTextView.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .start()
                        
                        // Animate buttons
                        Handler(Looper.getMainLooper()).postDelayed({
                            btnCertificate.animate().alpha(1f).setDuration(300).start()
                            btnContinue.animate().alpha(1f).setDuration(300).setStartDelay(150).start()
                            btnTryAgain.animate().alpha(1f).setDuration(300).setStartDelay(300).start()
                        }, 300)
                    }, 300)
                }, 400)
            }, 400)
        }, 200)
    }
    
    override fun onBackPressed() {
        val intent = Intent(this, CourseDetailActivity::class.java)
        intent.putExtra("COURSE_ID", getIntent().getIntExtra("COURSE_ID", 0))
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}
