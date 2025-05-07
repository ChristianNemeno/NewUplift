package com.android.newuplift.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newuplift.api.RetrofitClient
import com.android.newuplift.utility.Quote
import com.android.newuplift.utility.QuoteCache
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _currentQuote = MutableLiveData<Quote?>()
    val currentQuote: LiveData<Quote?> get() = _currentQuote

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // adding throtling variables
    private var lastApiCallTime = 0L
    private val API_CALL_COOLDOWN = 2000L // 2 seconds

    fun fetchInitialQuoteIfNeeded(tag: String) {
        // Only fetch if no quote is currently set
        if (_currentQuote.value == null) {
            fetchQuoteFromApi(tag)
        }
    }

    fun fetchNewQuote(tag: String) {
        fetchQuoteFromApi(tag)
    }

    private fun fetchQuoteFromApi(tag: String) {
        val currentTime = System.currentTimeMillis()

        // If API call too frequent, use cache directly
        if (currentTime - lastApiCallTime < API_CALL_COOLDOWN) {
            Log.d("HomeViewModel", "API call too frequent, using cache")
            val cachedQuote = QuoteCache.getRandomQuote(tag)
            _currentQuote.postValue(cachedQuote)
            _errorMessage.postValue("Getting quotes too quickly. Using offline quotes.")
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val quote = RetrofitClient.api.getRandomQuote(tag)
                _currentQuote.postValue(quote)
                _errorMessage.postValue(null)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching quote: ${e.message}")
                e.printStackTrace()
                val cachedQuote = QuoteCache.getRandomQuote(tag)
                _currentQuote.postValue(cachedQuote)
                //_errorMessage.postValue("Error fetching quote. Using offline quotes.")
                // _errorMessage.postValue("Error fetching quote: ${e.message}")
                _errorMessage.postValue(null)
            } finally {
                _isLoading.value = false
            }
        }
    }
}