package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    
    private val courseDataProvider = CourseDataProvider
    
    // LiveData for recommended courses
    private val _recommendedCourses = MutableLiveData<List<Course>>()
    val recommendedCourses: LiveData<List<Course>> = _recommendedCourses
    
    // LiveData for trending courses
    private val _trendingCourses = MutableLiveData<List<Course>>()
    val trendingCourses: LiveData<List<Course>> = _trendingCourses
    
    // LiveData for featured banner course
    private val _featuredCourse = MutableLiveData<Course>()
    val featuredCourse: LiveData<Course> = _featuredCourse
    
    // Initialize ViewModel
    init {
        loadCourses()
    }
    
    // Load courses from data provider
    private fun loadCourses() {
        // Load recommended courses
        _recommendedCourses.value = courseDataProvider.getPopularCourses()
        
        // Load trending courses
        _trendingCourses.value = courseDataProvider.getTrendingCourses()
        
        // Set a featured course for banner
        val allCourses = courseDataProvider.getAllCourses()
        if (allCourses.isNotEmpty()) {
            _featuredCourse.value = allCourses.firstOrNull { it.rating >= 4.5f } ?: allCourses[0]
        }
    }
    
    // Refresh data
    fun refreshCourses() {
        loadCourses()
    }
    
    // Filter courses by category
    fun filterCoursesByCategory(category: String): List<Course> {
        return courseDataProvider.getAllCourses().filter { it.category.equals(category, ignoreCase = true) }
    }
    
    // Search courses by query
    fun searchCourses(query: String): List<Course> {
        if (query.isBlank()) return emptyList()
        
        val allCourses = courseDataProvider.getAllCourses()
        return allCourses.filter {
            it.title.contains(query, ignoreCase = true) || 
            it.instructor.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}
