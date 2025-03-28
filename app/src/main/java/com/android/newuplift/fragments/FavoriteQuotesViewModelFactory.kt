package com.android.newuplift.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.newuplift.database.DatabaseHelper

class FavoriteQuotesViewModelFactory(private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteQuotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteQuotesViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}