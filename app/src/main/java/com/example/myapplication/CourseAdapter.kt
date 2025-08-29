package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(private val courses: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCourse: ImageView = view.findViewById(R.id.ivCourse)
        val tvCourseTitle: TextView = view.findViewById(R.id.tvCourseTitle)
        val tvCourseCategory: TextView = view.findViewById(R.id.tvCourseCategory)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvLessonsCount: TextView = view.findViewById(R.id.tvLessonsCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        
        holder.ivCourse.setImageResource(course.imageUrl)
        holder.tvCourseTitle.text = course.title
        holder.tvCourseCategory.text = course.instructor
        holder.tvRating.text = course.rating.toString()
        holder.tvLessonsCount.text = "${course.numReviews} reviews"
        
        // Add level indicator (Beginner, Intermediate, Advanced)
        val levelIndicator = when (course.level) {
            "Beginner" -> R.drawable.level_beginner_badge
            "Intermediate" -> R.drawable.level_intermediate_badge
            "Advanced" -> R.drawable.level_advanced_badge
            else -> R.drawable.level_beginner_badge
        }
        // If you have an ImageView for the level
        // holder.ivLevel.setImageResource(levelIndicator)
        
        // Set onClick listener for course item
        holder.itemView.setOnClickListener {
            // Navigate to course detail
            val intent = Intent(holder.itemView.context, CourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)
            holder.itemView.context.startActivity(intent)
            // holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courses.size
}
