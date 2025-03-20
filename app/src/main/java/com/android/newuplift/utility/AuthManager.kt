package com.android.newuplift.utility

import android.content.Context

object AuthManager {
    private var _currentUserId : Int = -1
    private var _isLoggedIn : Boolean = false

    val currentUserId: Int
        get() = _currentUserId

    val isLoggedIn: Boolean
        get() = _isLoggedIn

    fun login(userId: Int, context : Context) {
        _currentUserId = userId
        _isLoggedIn = true

        val prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("currentUserId", _currentUserId).apply()

    }

    // Clear user ID and login status on logout
    fun logout(context : Context) {
        _currentUserId = -1
        _isLoggedIn = false

        val prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun initialize(context: Context) {
        val prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        _currentUserId = prefs.getInt("currentUserId", -1)
        _isLoggedIn = _currentUserId != -1
    }
}