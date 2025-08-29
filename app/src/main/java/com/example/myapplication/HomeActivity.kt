package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var userManager: UserManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var viewModel: HomeViewModel
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize managers and ViewModel
        userManager = UserManager.getInstance(this)
        notificationManager = NotificationManager.getInstance(this)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        
        // Initialize permission launcher
        notificationPermissionLauncher = NotificationPermissionUtil.registerForPermissionResult(
            this,
            onGranted = { 
                // Permission granted
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            },
            onDenied = {
                // Permission denied
                Toast.makeText(this, 
                    "Notification permission denied. You won't receive course notifications.", 
                    Toast.LENGTH_LONG).show()
            }
        )
        
        // Check if user is logged in
        if (userManager.getCurrentUser() == null) {
            // Redirect to sign in activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        
        setContentView(R.layout.activity_home)

        // Set up notification button click
        val btnNotification = findViewById<ImageView>(R.id.ivNotification)
        
        // Update notification badge if needed
        updateNotificationBadge(btnNotification)
        
        btnNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        // Set up recommended courses RecyclerView
        val rvRecommended = findViewById<RecyclerView>(R.id.rvRecommendedCourses)
        rvRecommended.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        
        // Set up trending courses RecyclerView
        val rvTrending = findViewById<RecyclerView>(R.id.rvTrendingCourses)
        rvTrending.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Observe LiveData from ViewModel
        viewModel.recommendedCourses.observe(this) { courses ->
            rvRecommended.adapter = CourseAdapter(courses)
        }

        viewModel.trendingCourses.observe(this) { courses ->
            rvTrending.adapter = CourseAdapter(courses)
        }
        
        // Set up pull to refresh if available
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout?>(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.refreshCourses()
            swipeRefreshLayout.isRefreshing = false
        }
        
        // Set up search bar click
        setupSearchBar()
        
        // Set up View All buttons
        setupViewAllButtons()
        
        // Set up navigation
        setupNavigation()
        
        // Request notification permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationPermissionUtil.requestNotificationPermissionIfNeeded(
                this,
                notificationPermissionLauncher
            )
        }
    }
    
    private fun updateNotificationBadge(notificationIcon: ImageView) {
        val unreadCount = notificationManager.getUnreadCount()
        if (unreadCount > 0) {
            // Show badge with count (you would need to implement this UI component)
            val badge = findViewById<TextView>(R.id.tvNotificationBadge)
            badge?.visibility = View.VISIBLE
            badge?.text = if (unreadCount > 99) "99+" else unreadCount.toString()
        } else {
            // Hide badge
            findViewById<TextView>(R.id.tvNotificationBadge)?.visibility = View.GONE
        }
    }
    
    private fun setupNavigation() {
        val btnHome = findViewById<ImageButton>(R.id.btnNavHome)
        val btnSearch = findViewById<ImageButton>(R.id.btnNavSearch)
        val btnCourses = findViewById<ImageButton>(R.id.btnNavCourses)
        val btnProfile = findViewById<ImageButton>(R.id.btnNavProfile)
        
        // Set listeners for navigation buttons
        btnHome.setOnClickListener {
            // Already on home
        }
        
        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        
        btnCourses.setOnClickListener {
            startActivity(Intent(this, MyCoursesActivity::class.java))
        }
        
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
    
    // Update notification badge when resuming activity
    override fun onResume() {
        super.onResume()
        val btnNotification = findViewById<ImageView>(R.id.ivNotification)
        updateNotificationBadge(btnNotification)
    }
    
    private fun setupSearchBar() {
        // Make the entire search card clickable
        val searchCard = findViewById<View>(R.id.cardSearch)
        searchCard.setOnClickListener {
            // Navigate to SearchActivity when search bar is clicked
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }
    
    private fun setupViewAllButtons() {
        // Set up View All for Recommended Courses
        val viewAllRecommended = findViewById<TextView>(R.id.tvViewAllRecommended)
        viewAllRecommended.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("CATEGORY", "recommended")
            startActivity(intent)
        }
        
        // Set up View All for Trending Courses
        val viewAllTrending = findViewById<TextView>(R.id.tvViewAllTrending)
        viewAllTrending.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("CATEGORY", "trending")
            startActivity(intent)
        }
    }
}
