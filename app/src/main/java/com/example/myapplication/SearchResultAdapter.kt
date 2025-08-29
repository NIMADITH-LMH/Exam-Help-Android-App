package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultAdapter(private val courses: List<Course>) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCourse: ImageView = view.findViewById(R.id.ivSearchResultImage)
        val tvCourseTitle: TextView = view.findViewById(R.id.tvSearchResultTitle)
        val tvCourseCategory: TextView = view.findViewById(R.id.tvSearchResultCategory)
        val tvRating: TextView = view.findViewById(R.id.tvSearchResultRating)
        val tvLessonsCount: TextView = view.findViewById(R.id.tvSearchResultLessons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val course = courses[position]
        
        holder.ivCourse.setImageResource(course.imageUrl)
        holder.tvCourseTitle.text = course.title
        holder.tvCourseCategory.text = course.instructor
        holder.tvRating.text = String.format("%.1f", course.rating)
        holder.tvLessonsCount.text = "${course.numReviews} reviews"
        
        // Set onClick listener for course item
        holder.itemView.setOnClickListener {
            // Navigate to course detail
            val intent = Intent(holder.itemView.context, CourseDetailActivity::class.java)
            intent.putExtra("COURSE_TITLE", course.title)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courses.size
}
