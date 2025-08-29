package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UserManager {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_CURRENT_USER = "current_user"
    private const val KEY_USERS = "users"
    
    // User levels
    enum class UserLevel {
        ELEMENTARY,
        JUNIOR,
        HIGH_SCHOOL,
        COLLEGE,
        PROFESSIONAL
    }
    
    // Singleton instance
    private var instance: UserManager? = null
    
    // Get singleton instance
    fun getInstance(context: Context): UserManager {
        if (instance == null) {
            instance = UserManager
            instance!!.initialize(context.applicationContext)
        }
        return instance!!
    }
    
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    
    private fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    // Register a new user
    fun registerUser(user: User): Boolean {
        val users = getAllUsers().toMutableList()
        
        // Check if username or email already exists
        if (users.any { it.username == user.username || it.email == user.email }) {
            return false
        }
        
        users.add(user)
        saveUsers(users)
        return true
    }
    
    // Login user
    fun loginUser(usernameOrEmail: String, password: String): User? {
        val users = getAllUsers()
        val user = users.find { 
            (it.username == usernameOrEmail || it.email == usernameOrEmail) && it.password == password 
        }
        
        if (user != null) {
            setCurrentUser(user)
        }
        
        return user
    }
    
    // Logout current user
    fun logout() {
        sharedPreferences.edit().remove(KEY_CURRENT_USER).apply()
    }
    
    // Get current logged in user
    fun getCurrentUser(): User? {
        val userJson = sharedPreferences.getString(KEY_CURRENT_USER, null) ?: return null
        return gson.fromJson(userJson, User::class.java)
    }
    
    // Set current user
    private fun setCurrentUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(KEY_CURRENT_USER, userJson).apply()
    }
    
    // Get all registered users
    fun getAllUsers(): List<User> {
        val usersJson = sharedPreferences.getString(KEY_USERS, null) ?: return emptyList()
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(usersJson, type) ?: emptyList()
    }
    
    // Save users list
    private fun saveUsers(users: List<User>) {
        val usersJson = gson.toJson(users)
        sharedPreferences.edit().putString(KEY_USERS, usersJson).apply()
    }
    
    // Update user profile
    fun updateUserProfile(user: User): Boolean {
        val users = getAllUsers().toMutableList()
        val index = users.indexOfFirst { it.id == user.id }
        
        if (index != -1) {
            users[index] = user
            saveUsers(users)
            
            // If this is the current user, update current user too
            val currentUser = getCurrentUser()
            if (currentUser != null && currentUser.id == user.id) {
                setCurrentUser(user)
            }
            return true
        }
        return false
    }
    
    // Update notification settings
    fun updateNotificationSettings(enabled: Boolean) {
        val currentUser = getCurrentUser() ?: return
        currentUser.notificationsEnabled = enabled
        updateUserProfile(currentUser)
    }
    
    // Check if a username is available
    fun isUsernameAvailable(username: String): Boolean {
        return !getAllUsers().any { it.username == username }
    }
    
    // Check if an email is available
    fun isEmailAvailable(email: String): Boolean {
        return !getAllUsers().any { it.email == email }
    }
}
