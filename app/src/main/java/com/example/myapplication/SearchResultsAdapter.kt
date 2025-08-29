package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultsAdapter(private val courses: List<Course>) : 
    RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseTitle: TextView = view.findViewById(R.id.tvSearchResultTitle)
        val courseInstructor: TextView = view.findViewById(R.id.tvSearchResultCategory)
        val courseImage: ImageView = view.findViewById(R.id.ivSearchResultImage)
        val courseRating: TextView = view.findViewById(R.id.tvSearchResultRating)
        val coursePrice: TextView = view.findViewById(R.id.tvSearchResultPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val course = courses[position]
        
        holder.courseTitle.text = course.title
        holder.courseInstructor.text = "By ${course.instructor}"
        holder.courseImage.setImageResource(course.imageResId)
        holder.courseRating.text = course.rating.toString()
        holder.coursePrice.text = course.price
        
        // Set click listener to navigate to course detail
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courses.size
}
