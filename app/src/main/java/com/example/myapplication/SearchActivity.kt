package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var recyclerViewResults: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var recyclerViewRecentSearches: RecyclerView
    private lateinit var noResultsView: View
    private lateinit var categoriesSection: View
    private lateinit var recentSearchesSection: View
    private lateinit var searchResultsHeader: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize views
        searchEditText = findViewById(R.id.editTextSearch)
        recyclerViewResults = findViewById(R.id.recyclerViewSearchResults)
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories)
        recyclerViewRecentSearches = findViewById(R.id.recyclerViewRecentSearches)
        noResultsView = findViewById(R.id.noResultsLayout)
        categoriesSection = findViewById(R.id.categoriesSection)
        recentSearchesSection = findViewById(R.id.recentSearchesSection)
        searchResultsHeader = findViewById(R.id.tvSearchResultsHeader)
        
        // Set up back button
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
        
        // Set up RecyclerViews
        setupCategoriesRecyclerView()
        setupRecentSearchesRecyclerView()
        setupSearchResultsRecyclerView()
        
        // Set up search functionality
        setupSearch()
        
        // Check if coming from a "View All" button
        checkForViewAllIntent()
    }
    
    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    // Show categories and recent searches when query is empty
                    showInitialState()
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun performSearch(query: String) {
        // Hide categories and recent searches
        categoriesSection.visibility = View.GONE
        recentSearchesSection.visibility = View.GONE
        
        // Show results and header
        recyclerViewResults.visibility = View.VISIBLE
        searchResultsHeader.visibility = View.VISIBLE
        
        // Save this search
        if (query.length >= 3) {
            saveRecentSearch(query)
        }
        
        // Search through all available courses
        val allCourses = CourseDataProvider.getPopularCourses() + CourseDataProvider.getTrendingCourses()
        val searchResults = allCourses.filter { 
            it.title.contains(query, true) || 
            it.instructor.contains(query, true) || 
            it.category.contains(query, true) || 
            it.level.contains(query, true)
        }
        
        if (searchResults.isNotEmpty()) {
            recyclerViewResults.adapter = SearchResultAdapter(searchResults)
            noResultsView.visibility = View.GONE
        } else {
            recyclerViewResults.visibility = View.GONE
            searchResultsHeader.visibility = View.GONE
            noResultsView.visibility = View.VISIBLE
        }
    }
    
    private fun showInitialState() {
        categoriesSection.visibility = View.VISIBLE
        recentSearchesSection.visibility = View.VISIBLE
        recyclerViewResults.visibility = View.GONE
        searchResultsHeader.visibility = View.GONE
        noResultsView.visibility = View.GONE
    }
    
    private fun setupCategoriesRecyclerView() {
        recyclerViewCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        
        val categories = listOf(
            Category(1, "Mathematics", R.drawable.ic_category_math),
            Category(2, "Science", R.drawable.ic_category_science),
            Category(3, "Language Arts", R.drawable.ic_category_language),
            Category(4, "Social Studies", R.drawable.ic_category_social),
            Category(5, "Computer Science", R.drawable.ic_category_cs)
        )
        
        recyclerViewCategories.adapter = CategoryAdapter(categories) { category ->
            // When a category is selected, search for it
            searchEditText.setText(category.name)
            searchEditText.setSelection(category.name.length)
            performSearch(category.name)
        }
    }
    
    private fun getRecentSearches(): List<String> {
        val sharedPref = getSharedPreferences("search_prefs", MODE_PRIVATE)
        val searchesJson = sharedPref.getString("recent_searches", null) ?: return emptyList()
        
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(searchesJson, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun saveRecentSearch(query: String) {
        val recentSearches = getRecentSearches().toMutableList()
        
        // Remove if already exists (to move to top)
        recentSearches.remove(query)
        
        // Add to beginning of list
        recentSearches.add(0, query)
        
        // Keep only the most recent 10 searches
        val trimmedSearches = recentSearches.take(10)
        
        // Save to SharedPreferences
        val sharedPref = getSharedPreferences("search_prefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        editor.putString("recent_searches", gson.toJson(trimmedSearches))
        editor.apply()
        
        // Update UI
        setupRecentSearchesRecyclerView()
    }
    
    private fun setupRecentSearchesRecyclerView() {
        recyclerViewRecentSearches.layoutManager = LinearLayoutManager(this)
        val recentSearches = getRecentSearches()
        
        if (recentSearches.isEmpty()) {
            recentSearchesSection.visibility = View.GONE
        } else {
            recentSearchesSection.visibility = View.VISIBLE
            recyclerViewRecentSearches.adapter = RecentSearchAdapter(recentSearches) { searchTerm ->
                // When a recent search is selected
                searchEditText.setText(searchTerm)
                searchEditText.setSelection(searchTerm.length)
                performSearch(searchTerm)
            }
        }
    }
    
    private fun setupSearchResultsRecyclerView() {
        recyclerViewResults.layoutManager = LinearLayoutManager(this)
        recyclerViewResults.visibility = View.GONE
        
        // Add item decoration for better spacing
        val spacing = resources.getDimensionPixelSize(R.dimen.search_result_spacing)
        recyclerViewResults.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: android.graphics.Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = spacing / 2
                outRect.bottom = spacing / 2
            }
        })
    }
    
    private fun checkForViewAllIntent() {
        // Check if we're coming from a "View All" button
        val category = intent.getStringExtra("CATEGORY")
        
        if (category != null) {
            // Hide categories and recent searches
            categoriesSection.visibility = View.GONE
            recentSearchesSection.visibility = View.GONE
            
            // Show appropriate results based on category
            when (category) {
                "recommended" -> {
                    // Show all recommended courses
                    val allRecommendedCourses = CourseDataProvider.getPopularCourses()
                    showSearchResults(allRecommendedCourses)
                    
                    // Set title in search box
                    searchEditText.setText("Recommended Courses")
                }
                "trending" -> {
                    // Show all trending courses
                    val allTrendingCourses = CourseDataProvider.getTrendingCourses()
                    showSearchResults(allTrendingCourses)
                    
                    // Set title in search box
                    searchEditText.setText("Trending Courses")
                }
            }
        }
    }
    
    private fun showSearchResults(courses: List<Course>) {
        if (courses.isNotEmpty()) {
            // Show search results
            recyclerViewResults.visibility = View.VISIBLE
            noResultsView.visibility = View.GONE
            
            // Set adapter for results
            recyclerViewResults.adapter = SearchResultsAdapter(courses)
        } else {
            // Show no results view
            recyclerViewResults.visibility = View.GONE
            noResultsView.visibility = View.VISIBLE
        }
    }
}

// Using Category from separate file
