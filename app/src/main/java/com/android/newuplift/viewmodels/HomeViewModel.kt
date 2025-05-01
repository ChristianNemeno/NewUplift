package com.android.newuplift.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newuplift.api.RetrofitClient
import com.android.newuplift.utility.Quote
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _currentQuote = MutableLiveData<Quote?>()
    val currentQuote: LiveData<Quote?> get() = _currentQuote

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

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
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val quote = RetrofitClient.api.getRandomQuote(tag)
                _currentQuote.postValue(quote)
                _errorMessage.postValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue("Error fetching quote: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}