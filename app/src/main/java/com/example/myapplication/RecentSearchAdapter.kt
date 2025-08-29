package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentSearchAdapter(
    private val searches: List<String>,
    private val onSearchSelected: ((String) -> Unit)? = null
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {

    inner class RecentSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSearchTerm: TextView = view.findViewById(R.id.tvSearchTerm)
        val ivSearchIcon: ImageView = view.findViewById(R.id.ivSearchIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_search, parent, false)
        return RecentSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val search = searches[position]
        
        holder.tvSearchTerm.text = search
        
        holder.itemView.setOnClickListener {
            // Handle search term selection
            onSearchSelected?.invoke(search)
        }
    }

    override fun getItemCount(): Int = searches.size
}
