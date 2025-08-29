package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LessonDetailActivity : AppCompatActivity() {

    private var courseId: Int = 0
    private var lessonId: Int = 0
    private lateinit var course: Course
    private lateinit var lesson: Lesson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)

        // Get IDs from intent
        courseId = intent.getIntExtra("COURSE_ID", 0)
        lessonId = intent.getIntExtra("LESSON_ID", 0)
        
        // Find course and lesson
        course = findCourseById(courseId) ?: run {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        lesson = course.lessons.find { it.id == lessonId } ?: run {
            Toast.makeText(this, "Lesson not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        // Setup UI
        setupUI()
        
        // Setup back button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // Go back to course detail
        }
        
        // Setup next lesson button
        findViewById<Button>(R.id.btnNextLesson).setOnClickListener {
            moveToNextLesson()
        }
    }
    
    private fun setupUI() {
        // Set lesson title and content
        findViewById<TextView>(R.id.tvLessonTitle).text = lesson.title
        findViewById<TextView>(R.id.tvLessonContent).text = "This is the content for: ${lesson.title}"
        findViewById<TextView>(R.id.tvCourseName).text = course.title
    }
    
    private fun moveToNextLesson() {
        // Find the index of the current lesson
        val currentIndex = course.lessons.indexOfFirst { it.id == lessonId }
        
        if (currentIndex < course.lessons.size - 1) {
            // Move to the next lesson
            val nextLesson = course.lessons[currentIndex + 1]
            lessonId = nextLesson.id
            lesson = nextLesson
            setupUI()
            Toast.makeText(this, "Moved to next lesson", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "This is the last lesson", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun findCourseById(id: Int): Course? {
        // Search through all courses
        val allCourses = CourseDataProvider.getPopularCourses() + CourseDataProvider.getTrendingCourses()
        return allCourses.find { it.id == id }
    }
}
