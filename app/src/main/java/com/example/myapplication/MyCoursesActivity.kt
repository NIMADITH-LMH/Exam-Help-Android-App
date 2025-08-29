package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyCoursesActivity : AppCompatActivity() {

    private lateinit var rvMyCourses: RecyclerView
    private lateinit var rvCompletedCourses: RecyclerView
    private lateinit var tvNoEnrolledCourses: TextView
    private lateinit var tvNoCompletedCourses: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_courses)
        
        initViews()
        setupToolbar()
        setupRecyclerViews()
        setupNavigation()
    }
    
    private fun initViews() {
        rvMyCourses = findViewById(R.id.rvMyCourses)
        rvCompletedCourses = findViewById(R.id.rvCompletedCourses)
        tvNoEnrolledCourses = findViewById(R.id.tvNoEnrolledCourses)
        tvNoCompletedCourses = findViewById(R.id.tvNoCompletedCourses)
    }
    
    private fun setupToolbar() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerViews() {
        // Setup enrolled courses
        val enrolledCourses = getEnrolledCourses()
        if (enrolledCourses.isEmpty()) {
            tvNoEnrolledCourses.visibility = View.VISIBLE
            rvMyCourses.visibility = View.GONE
        } else {
            tvNoEnrolledCourses.visibility = View.GONE
            rvMyCourses.visibility = View.VISIBLE
            
            // Use GridLayoutManager with 2 columns for a centered grid layout
            val gridLayoutManager = GridLayoutManager(this, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // When there's only one item, make it take full width (2 spans)
                    return if (enrolledCourses.size == 1) 2 else 1
                }
            }
            rvMyCourses.layoutManager = gridLayoutManager
            rvMyCourses.adapter = CourseAdapter(enrolledCourses)
        }
        
        // Setup completed courses
        val completedCourses = getCompletedCourses()
        if (completedCourses.isEmpty()) {
            tvNoCompletedCourses.visibility = View.VISIBLE
            rvCompletedCourses.visibility = View.GONE
        } else {
            tvNoCompletedCourses.visibility = View.GONE
            rvCompletedCourses.visibility = View.VISIBLE
            
            // Use GridLayoutManager with 2 columns for a centered grid layout
            val gridLayoutManager = GridLayoutManager(this, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // When there's only one item, make it take full width (2 spans)
                    return if (completedCourses.size == 1) 2 else 1
                }
            }
            rvCompletedCourses.layoutManager = gridLayoutManager
            rvCompletedCourses.adapter = CourseAdapter(completedCourses)
        }
    }
    
    private fun setupNavigation() {
        // Setup bottom navigation
        findViewById<ImageView>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }
        
        findViewById<ImageView>(R.id.btnNavSearch).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        
        findViewById<ImageView>(R.id.btnNavCourses).setOnClickListener {
            // Already on My Courses
        }
        
        findViewById<ImageView>(R.id.btnNavProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
    
    // Sample data
    private fun getEnrolledCourses(): List<Course> {
        return listOf(
            Course(
                id = 1,
                title = "UI/UX Design Course",
                instructor = "John Smith",
                rating = 4.8f,
                numReviews = 485,
                price = "$49.99",
                imageUrl = R.drawable.img_course1,
                progress = 75
            ),
            Course(
                id = 2,
                title = "Flutter Development Bootcamp",
                instructor = "Sarah Johnson",
                rating = 4.7f,
                numReviews = 356,
                price = "$59.99",
                imageUrl = R.drawable.img_course2,
                progress = 45
            )
        )
    }
    
    private fun getCompletedCourses(): List<Course> {
        return listOf(
            Course(
                id = 3,
                title = "HTML & CSS Fundamentals",
                instructor = "Michael Brown",
                rating = 4.5f,
                numReviews = 720,
                price = "$29.99",
                imageUrl = R.drawable.img_course3,
                progress = 100
            )
        )
    }
}
