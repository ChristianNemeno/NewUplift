package com.android.newuplift.viewmodels

import android.util.Log // Import Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newuplift.api.RetrofitClient
import com.android.newuplift.utility.Quote
import com.android.newuplift.utility.FallbackQuotesProvider // Import the new provider
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _currentQuote = MutableLiveData<Quote?>()
    val currentQuote: LiveData<Quote?> get() = _currentQuote

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchInitialQuoteIfNeeded(tag: String) {
        if (_currentQuote.value == null) {
            fetchQuoteFromApi(tag)
        }
    }

    fun fetchNewQuote(tag: String) {
        fetchQuoteFromApi(tag)
    }

    private fun fetchQuoteFromApi(tag: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val quote = RetrofitClient.api.getRandomQuote(tag)
                _currentQuote.postValue(quote)
                _errorMessage.postValue(null) // Clear previous errors
            } catch (e: Exception) {
                Log.e("HomeViewModel", "API Error: ${e.message}", e) // Log the exception
                // Fallback to static quote
                val fallbackQuote = FallbackQuotesProvider.getRandomFallbackQuote()
                if (fallbackQuote != null) {
                    _currentQuote.postValue(fallbackQuote)
//                    _errorMessage.postValue("Showing an offline quote. Please check your internet connection.")
                } else {
                    // Handle case where even fallback fails (e.g., empty list)
                    _currentQuote.postValue(FallbackQuotesProvider.getDefaultErrorQuote())
//                    _errorMessage.postValue("Failed to fetch quote and no fallback available.")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}