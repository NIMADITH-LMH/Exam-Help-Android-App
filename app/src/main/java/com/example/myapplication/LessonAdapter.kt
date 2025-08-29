package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val onLessonClick: (lessonId: Int) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLessonTitle: TextView = view.findViewById(R.id.tvLessonTitle)
        val tvLessonDuration: TextView = view.findViewById(R.id.tvLessonDuration)
        val ivCompletionStatus: ImageView = view.findViewById(R.id.ivCompletionStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.tvLessonTitle.text = lesson.title
        holder.tvLessonDuration.text = lesson.duration
        
        // Show completion status
        if (lesson.isCompleted) {
            holder.ivCompletionStatus.setImageResource(R.drawable.ic_completed)
        } else {
            holder.ivCompletionStatus.setImageResource(R.drawable.ic_not_completed)
        }
        
        // Set click listener
        holder.itemView.setOnClickListener {
            onLessonClick(lesson.id)
        }
    }

    override fun getItemCount(): Int = lessons.size
}
