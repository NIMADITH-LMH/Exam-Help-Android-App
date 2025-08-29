package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.animation.PathInterpolatorCompat

class QuizActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var courseId: Int = 0
    private var quizId: Int = 0
    private var courseName: String = "Course"
    
    // Sample questions - in a real app, these would be loaded based on courseId and quizId
    private val questions = listOf(
        QuizQuestion(
            1,
            "What is 2 + 2?",
            listOf("3", "4", "5", "6"),
            1 // Correct answer index (0-based)
        ),
        QuizQuestion(
            2,
            "Which is the largest planet in our solar system?",
            listOf("Earth", "Mars", "Jupiter", "Venus"),
            2 // Correct answer index (0-based)
        ),
        QuizQuestion(
            3,
            "What is the capital of France?",
            listOf("London", "Berlin", "Paris", "Rome"),
            2 // Correct answer index (0-based)
        )
    )

    private var score = 0
    private lateinit var questionTextView: TextView
    private lateinit var radioGroupOptions: RadioGroup
    private lateinit var btnNext: Button
    private lateinit var btnClose: ImageButton
    private lateinit var questionNumberTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var countDownTimer: CountDownTimer
    private var timeRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Get data from intent
        courseId = intent.getIntExtra("COURSE_ID", 0)
        quizId = intent.getIntExtra("QUIZ_ID", 0)
        courseName = intent.getStringExtra("COURSE_NAME") ?: "Course"
        val isEnrolled = intent.getBooleanExtra("IS_ENROLLED", false)

        if (courseId == 0) {
            Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        // Check if user is enrolled in the course
        // Either the intent has IS_ENROLLED=true or the user is already enrolled according to our system
        val userCanAccessQuiz = isEnrolled || isUserEnrolledInCourse(courseId)
        
        if (!userCanAccessQuiz) {
            Toast.makeText(this, "Please enroll in the course to access quizzes", Toast.LENGTH_LONG).show()
            // Redirect to course details page
            val intent = Intent(this, CourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", courseId)
            startActivity(intent)
            finish()
            return
        }

        // Initialize views
        questionTextView = findViewById(R.id.tvQuestion)
        radioGroupOptions = findViewById(R.id.rgOptions)
        btnNext = findViewById(R.id.btnContinue)
        btnClose = findViewById(R.id.btnClose)
        questionNumberTextView = findViewById(R.id.tvQuestionCount)
        progressBar = findViewById(R.id.quizProgress)
        
        // Set initial progress
        updateProgressBar()
        
        // Start with animation
        animateQuestionAppear()
        
        displayQuestion()

        btnNext.setOnClickListener {
            // Check answer
            val selectedOptionId = radioGroupOptions.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val radioButton = findViewById<RadioButton>(selectedOptionId)
                val selectedIndex = radioGroupOptions.indexOfChild(radioButton)
                
                if (selectedIndex == questions[currentQuestionIndex].correctOptionIndex) {
                    score++
                    showCorrectAnswerAnimation(radioButton)
                } else {
                    showWrongAnswerAnimation(radioButton)
                }

                // Move to next question or finish with slight delay for animation
                radioButton.postDelayed({
                    currentQuestionIndex++
                    if (currentQuestionIndex < questions.size) {
                        animateQuestionTransition()
                        displayQuestion()
                        updateProgressBar()
                    } else {
                        // Quiz finished, show results
                        val intent = Intent(this, QuizResultActivity::class.java)
                        intent.putExtra("SCORE", score)
                        intent.putExtra("TOTAL", questions.size)
                        intent.putExtra("COURSE_NAME", courseName)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                }, 800) // Short delay for feedback animation
            } else {
                // Animate the radio group to indicate selection required
                val shake = ObjectAnimator.ofFloat(radioGroupOptions, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                shake.duration = 500
                shake.interpolator = PathInterpolatorCompat.create(0.22f, 0.61f, 0.36f, 1f)
                shake.start()
                
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            }
        }

        btnClose.setOnClickListener {
            // Show confirmation dialog before exiting
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit Quiz")
                .setMessage("Are you sure you want to exit the quiz? Your progress will be lost.")
                .setPositiveButton("Exit") { _, _ -> 
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
                .setNegativeButton("Stay", null)
                .show()
        }
    }

    private fun displayQuestion() {
        val question = questions[currentQuestionIndex]
        questionTextView.text = question.question
        questionNumberTextView.text = "Question ${currentQuestionIndex + 1} of ${questions.size}"

        // Clear previous options
        radioGroupOptions.removeAllViews()

        // Add radio buttons for each option
        for (i in question.options.indices) {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = question.options[i]
            radioButton.textSize = 16f
            
            // Apply style
            radioButton.setButtonDrawable(android.R.color.transparent) // Remove default radio button
            radioButton.setBackgroundResource(R.drawable.quiz_option_selector)
            radioButton.setPadding(32, 24, 32, 24)
            
            // Add to radio group with margin
            val layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = 24
            radioGroupOptions.addView(radioButton, layoutParams)
        }
        
        // Clear selection
        radioGroupOptions.clearCheck()
        
        // Update button text for last question
        if (currentQuestionIndex == questions.size - 1) {
            btnNext.text = "Finish"
        } else {
            btnNext.text = "Next"
        }
    }
    
    private fun updateProgressBar() {
        val progress = ((currentQuestionIndex + 1) * 100) / questions.size
        
        // Animate progress bar
        ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
    
    private fun animateQuestionAppear() {
        questionTextView.alpha = 0f
        radioGroupOptions.alpha = 0f
        questionNumberTextView.alpha = 0f
        
        questionTextView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
            
        questionNumberTextView.animate()
            .alpha(1f)
            .setStartDelay(100)
            .setDuration(500)
            .start()
            
        radioGroupOptions.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(200)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
    
    private fun animateQuestionTransition() {
        // Animate out
        questionTextView.animate()
            .alpha(0f)
            .translationX(-100f)
            .setDuration(300)
            .start()
            
        radioGroupOptions.animate()
            .alpha(0f)
            .translationX(-100f)
            .setDuration(300)
            .withEndAction {
                questionTextView.translationX = 100f
                radioGroupOptions.translationX = 100f
                
                // Animate in
                questionTextView.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(300)
                    .start()
                    
                radioGroupOptions.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
    
    private fun showCorrectAnswerAnimation(radioButton: RadioButton) {
        // Flash green background
        val colorAnimator = ObjectAnimator.ofArgb(
            radioButton,
            "textColor",
            getColor(android.R.color.black),
            getColor(android.R.color.holo_green_dark),
            getColor(android.R.color.black)
        )
        colorAnimator.duration = 800
        colorAnimator.start()
    }
    
    private fun showWrongAnswerAnimation(radioButton: RadioButton) {
        // Flash red background
        val colorAnimator = ObjectAnimator.ofArgb(
            radioButton,
            "textColor",
            getColor(android.R.color.black),
            getColor(android.R.color.holo_red_dark),
            getColor(android.R.color.black)
        )
        colorAnimator.duration = 800
        colorAnimator.start()
    }
    
    override fun onBackPressed() {
        btnClose.performClick()
    }
    
    // Helper function to check if the user is enrolled in a course
    private fun isUserEnrolledInCourse(courseId: Int): Boolean {
        // In a real app, this would check a database or API
        // For demo purposes, we'll check shared preferences or user manager
        // But for now, we're making the function non-blocking
        return true
    }
}
