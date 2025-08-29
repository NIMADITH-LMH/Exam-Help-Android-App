package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PaymentHistoryActivity : AppCompatActivity() {
    
    private lateinit var rvPayments: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_history)
        
        setupToolbar()
        setupPaymentHistory()
    }
    
    private fun setupToolbar() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }
    
    private fun setupPaymentHistory() {
        rvPayments = findViewById(R.id.rvPayments)
        
        val payments = getPaymentHistory()
        
        rvPayments.layoutManager = LinearLayoutManager(this)
        rvPayments.adapter = PaymentAdapter(payments)
    }
    
    private fun getPaymentHistory(): List<PaymentItem> {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        
        return listOf(
            PaymentItem(
                courseTitle = "UI/UX Design Course",
                amount = "$49.99",
                date = dateFormat.format(calendar.time),
                paymentMethod = "Credit Card (**** 4582)",
                status = "Completed"
            ),
            
            PaymentItem(
                courseTitle = "Flutter Development Bootcamp",
                amount = "$59.99",
                date = dateFormat.format(calendar.apply { add(Calendar.DAY_OF_MONTH, -15) }.time),
                paymentMethod = "PayPal",
                status = "Completed"
            ),
            
            PaymentItem(
                courseTitle = "HTML & CSS Fundamentals",
                amount = "$29.99",
                date = dateFormat.format(calendar.apply { add(Calendar.DAY_OF_MONTH, -45) }.time),
                paymentMethod = "Credit Card (**** 4582)",
                status = "Completed"
            )
        )
    }
}

// Using PaymentItem from separate file
