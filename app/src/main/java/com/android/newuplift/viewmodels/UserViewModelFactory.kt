package com.android.newuplift.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.newuplift.database.QuoteDao


class UserViewModelFactory(private val quoteDao: QuoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(quoteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}