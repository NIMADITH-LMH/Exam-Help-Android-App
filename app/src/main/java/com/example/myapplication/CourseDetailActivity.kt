package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CourseDetailActivity : AppCompatActivity() {

    private var courseId: Int = 0
    private lateinit var course: Course
    private var isEnrolled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        // Get course ID from intent
        courseId = intent.getIntExtra("COURSE_ID", 1)
        
        // Find the course using ID
        course = findCourseById(courseId) ?: run {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        // Set course details
        setupCourseDetails()
        
        // Set up RecyclerView for lessons
        setupLessonsRecyclerView()

        // Setup quiz button with animation and error handling
        val btnTakeQuiz = findViewById<Button>(R.id.btnTakeQuiz)
        btnTakeQuiz.setOnClickListener {
            try {
                // Scale animation on button press
                btnTakeQuiz.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        btnTakeQuiz.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                            
                        // Use the class-level isEnrolled variable
                        
                        // Launch quiz with proper data including enrollment status
                        val intent = Intent(this, QuizActivity::class.java)
                        intent.putExtra("COURSE_ID", courseId)
                        intent.putExtra("QUIZ_ID", 1) // Default to first quiz
                        intent.putExtra("COURSE_NAME", course.title)
                        intent.putExtra("IS_ENROLLED", isEnrolled)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                    .start()
            } catch (e: Exception) {
                // Handle any exceptions to prevent crashes
                Toast.makeText(this, "Sorry, quiz is not available at the moment. Try again later.", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Setup buttons for enrollment or starting course
        val btnStartCourse = findViewById<Button>(R.id.btnStartCourse)
        val btnEnrollNow = findViewById<Button>(R.id.btnEnrollNow)
        
        // Check if the user is already enrolled from intent or payment completion
        isEnrolled = intent.getBooleanExtra("IS_ENROLLED", false) || 
                    intent.getBooleanExtra("PAYMENT_COMPLETE", false)
                    
        // Show enrollment success notification if payment was just completed
        if (intent.getBooleanExtra("PAYMENT_COMPLETE", false)) {
            showEnrollmentSuccessNotification()
        }
        
        if (isEnrolled) {
            // User is enrolled, show Start Course button
            btnStartCourse.visibility = View.VISIBLE
            btnEnrollNow.visibility = View.GONE
            
            btnStartCourse.setOnClickListener {
                // Navigate to the first lesson
                Toast.makeText(this, "Starting course: ${course.title}", Toast.LENGTH_SHORT).show()
                
                if (course.lessons.isNotEmpty()) {
                    val intent = Intent(this, LessonDetailActivity::class.java)
                    intent.putExtra("COURSE_ID", courseId)
                    intent.putExtra("LESSON_ID", course.lessons[0].id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        } else {
            // User is not enrolled, show Enroll Now button
            btnStartCourse.visibility = View.GONE
            btnEnrollNow.visibility = View.VISIBLE
            
            btnEnrollNow.setOnClickListener {
                // Animate the button when clicked
                btnEnrollNow.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        btnEnrollNow.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                            
                        // Navigate to payment screen
                        val intent = Intent(this, PaymentActivity::class.java)
                        intent.putExtra("COURSE_ID", courseId)
                        intent.putExtra("COURSE_NAME", course.title)
                        intent.putExtra("COURSE_PRICE", findViewById<TextView>(R.id.tvPrice).text.toString())
                        intent.putExtra("COURSE_DURATION", "12 weeks") // This should come from the course data
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                    .start()
            }
        }
        
        // Handle payment complete flow
        if (intent.getBooleanExtra("PAYMENT_COMPLETE", false)) {
            Toast.makeText(this, "Enrollment successful! You now have access to all course materials.", Toast.LENGTH_LONG).show()
            isEnrolled = true
            btnStartCourse.visibility = View.VISIBLE
            btnEnrollNow.visibility = View.GONE
        }

        // Setup back button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // Go back to previous activity
        }
    }

    private fun setupCourseDetails() {
        // Set course title and other details
        findViewById<TextView>(R.id.tvCourseTitle).text = course.title
        findViewById<TextView>(R.id.tvInstructor).text = "Instructor: ${course.instructor}"
        findViewById<TextView>(R.id.tvRating).text = course.rating.toString()
        findViewById<TextView>(R.id.tvReviews).text = "(${course.numReviews} reviews)"
        findViewById<TextView>(R.id.tvPrice).text = course.price
        findViewById<TextView>(R.id.tvDescription).text = course.description
        findViewById<ImageView>(R.id.ivCourseImage).setImageResource(course.imageUrl)
        
        // Set level indicator
        val levelBadge = when(course.level) {
            "Beginner" -> R.drawable.level_beginner_badge
            "Intermediate" -> R.drawable.level_intermediate_badge
            "Advanced" -> R.drawable.level_advanced_badge
            else -> R.drawable.level_beginner_badge
        }
        
        findViewById<TextView>(R.id.tvLevel)?.apply {
            text = course.level
            background = getDrawable(levelBadge)
        }
    }
    
    private fun setupLessonsRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvLessons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Set up adapter with lessons data
        recyclerView.adapter = LessonAdapter(course.lessons) { lessonId ->
            // Handle lesson click
            val intent = Intent(this, LessonDetailActivity::class.java)
            intent.putExtra("COURSE_ID", courseId)
            intent.putExtra("LESSON_ID", lessonId)
            startActivity(intent)
        }
    }
    
    private fun findCourseById(id: Int): Course? {
        // Search through all courses
        val allCourses = CourseDataProvider.getPopularCourses() + CourseDataProvider.getTrendingCourses()
        return allCourses.find { it.id == id }
    }
    
    private fun showEnrollmentSuccessNotification() {
        // Create notification channel
        NotificationUtils.createNotificationChannel(this)
        
        // Show enrollment success notification
        NotificationUtils.showEnrollmentSuccessNotification(this, course.title)
    }
}
