package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class PaymentActivity : AppCompatActivity() {

    private var courseId: Int = 0
    private var courseName: String = ""
    private var coursePrice: String = ""
    private var courseDuration: String = ""
    private var selectedPaymentMethod: String = "Credit Card" // Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)

        // Get course information from intent
        courseId = intent.getIntExtra("COURSE_ID", 0)
        courseName = intent.getStringExtra("COURSE_NAME") ?: "Course Name"
        coursePrice = intent.getStringExtra("COURSE_PRICE") ?: "$99.99"
        courseDuration = intent.getStringExtra("COURSE_DURATION") ?: "12 weeks"
        
        // Set course details in UI
        findViewById<TextView>(R.id.tvCourseTitle).text = courseName
        findViewById<TextView>(R.id.tvCourseDuration).text = "Duration: $courseDuration"
        findViewById<TextView>(R.id.tvCoursePrice).text = coursePrice
        
        setupPaymentMethodSelection()
    }
    
    private fun setupPaymentMethodSelection() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnContinue = findViewById<Button>(R.id.btnContinue)
        val radioGroup = findViewById<RadioGroup>(R.id.rgPaymentMethods)
        
        // Set click listener for back button
        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        
        // Determine selected payment method
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedPaymentMethod = when (checkedId) {
                R.id.rbCreditCard -> "Credit Card"
                R.id.rbPaypal -> "PayPal"
                R.id.rbGooglePay -> "Google Pay"
                R.id.rbApplePay -> "Apple Pay"
                else -> "Credit Card"
            }
        }
        
        // Set click listener for Continue button
        btnContinue.setOnClickListener {
            when (selectedPaymentMethod) {
                "Credit Card" -> {
                    // Show credit card entry screen
                    showCreditCardScreen()
                }
                "PayPal" -> {
                    // In a real app, this would redirect to PayPal
                    simulateExternalPaymentProcess("PayPal")
                }
                "Google Pay" -> {
                    // In a real app, this would use Google Pay API
                    simulateExternalPaymentProcess("Google Pay")
                }
                "Apple Pay" -> {
                    // In a real app, this would use Apple Pay API
                    simulateExternalPaymentProcess("Apple Pay")
                }
            }
        }
        
        // Add animations to payment method cards
        animatePaymentOptions()
    }
    
    private fun showCreditCardScreen() {
        setContentView(R.layout.activity_credit_card)
        
        // Set title to show the selected course
        val tvCreditCard = findViewById<TextView>(R.id.tvCreditCard)
        tvCreditCard.text = "Pay for $courseName"

        // Get references to UI elements
        val etCardNumber = findViewById<EditText>(R.id.etCardNumber)
        val etCardName = findViewById<EditText>(R.id.etCardHolder)
        val spinnerMonth = findViewById<Spinner>(R.id.spinnerMonth)
        val spinnerYear = findViewById<Spinner>(R.id.spinnerYear)
        val etCvv = findViewById<EditText>(R.id.etCvv)
        val btnPay = findViewById<Button>(R.id.btnBecomeMember)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        
        // Update button text to show price
        btnPay.text = "Pay $coursePrice"

        // Set click listener for Pay button
        btnPay.setOnClickListener {
            // Validate input
            val expiry = spinnerMonth.selectedItem.toString() + "/" + spinnerYear.selectedItem.toString()
            if (validatePaymentDetails(etCardNumber.text.toString(), etCardName.text.toString(), 
                                        expiry, etCvv.text.toString())) {
                // Process payment - in a real app, this would be handled securely
                processSuccessfulPayment()
            }
        }

        // Set click listener for Back button
        btnBack.setOnClickListener {
            // Go back to payment method selection
            onCreate(null)
        }
    }
    
    private fun simulateExternalPaymentProcess(paymentProvider: String) {
        Toast.makeText(this, "Redirecting to $paymentProvider...", Toast.LENGTH_SHORT).show()
        
        // In a real app, we would redirect to the external payment provider here
        // For demo purposes, we'll just simulate a successful payment after a short delay
        findViewById<Button>(R.id.btnContinue).isEnabled = false
        
        // Simulate processing time
        findViewById<Button>(R.id.btnContinue).postDelayed({
            processSuccessfulPayment()
        }, 1500)
    }
    
    private fun processSuccessfulPayment() {
        // Show success message
        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
        
        // Show notification
        showPaymentSuccessNotification()
        
        // Navigate to course details with enrollment status updated
        val intent = Intent(this, CourseDetailActivity::class.java)
        intent.putExtra("COURSE_ID", courseId)
        intent.putExtra("IS_ENROLLED", true)
        intent.putExtra("PAYMENT_COMPLETE", true)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    
    private fun animatePaymentOptions() {
        val cardCreditCard = findViewById<CardView>(R.id.cardCreditCard)
        val cardPaypal = findViewById<CardView>(R.id.cardPaypal)
        val cardGooglePay = findViewById<CardView>(R.id.cardGooglePay)
        val cardApplePay = findViewById<CardView>(R.id.cardApplePay)
        
        val cards = listOf(cardCreditCard, cardPaypal, cardGooglePay, cardApplePay)
        
        // Set initial states
        cards.forEach { 
            it.translationX = -1000f
            it.alpha = 0f
        }
        
        // Create animation sequence
        val duration = 300L
        val startDelay = 100L
        
        // Animate each card with a delay
        cards.forEachIndexed { index, card ->
            val slideIn = ObjectAnimator.ofFloat(card, "translationX", -1000f, 0f)
            val fadeIn = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f)
            
            val animSet = AnimatorSet()
            animSet.playTogether(slideIn, fadeIn)
            animSet.duration = duration
            animSet.startDelay = startDelay * index
            animSet.interpolator = AccelerateDecelerateInterpolator()
            animSet.start()
        }
    }

    private fun showPaymentSuccessNotification() {
        // Create notification channel
        NotificationUtils.createNotificationChannel(this)
        
        // Show payment success notification
        NotificationUtils.showPaymentSuccessNotification(this, coursePrice, courseName)
    }
    
    private fun validatePaymentDetails(cardNumber: String, cardName: String, expiry: String, cvv: String): Boolean {
        // Simple validation - in a real app, you'd want more robust validation
        if (cardNumber.isEmpty() || cardNumber.length < 16) {
            Toast.makeText(this, "Please enter a valid card number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (cardName.isEmpty()) {
            Toast.makeText(this, "Please enter the name on card", Toast.LENGTH_SHORT).show()
            return false
        }

        if (expiry.isEmpty() || !expiry.contains("/")) {
            Toast.makeText(this, "Please enter a valid expiry date (MM/YY)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (cvv.isEmpty() || cvv.length < 3) {
            Toast.makeText(this, "Please enter a valid CVV", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    
    override fun onBackPressed() {
        // If on credit card screen, go back to payment method selection
        if (findViewById<EditText>(R.id.etCardNumber) != null) {
            onCreate(null)
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}
