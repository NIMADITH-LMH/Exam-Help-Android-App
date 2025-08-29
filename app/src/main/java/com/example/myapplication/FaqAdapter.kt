package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class FaqAdapter(private val items: List<FaqItem>) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.tvQuestion)
        private val answerText: TextView = itemView.findViewById(R.id.tvAnswer)
        private val expandIcon: ImageView = itemView.findViewById(R.id.ivExpand)
        private val container: ConstraintLayout = itemView.findViewById(R.id.containerFaq)

        fun bind(item: FaqItem) {
            questionText.text = item.question
            answerText.text = item.answer
            
            // Set initial visibility based on expanded state
            answerText.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
            expandIcon.setImageResource(if (item.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            
            container.setOnClickListener {
                item.isExpanded = !item.isExpanded
                answerText.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
                expandIcon.setImageResource(if (item.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            }
        }
    }
}
