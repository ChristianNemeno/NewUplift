package com.android.newuplift.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.Quote
import com.android.newuplift.utility.QuoteType
import kotlinx.coroutines.launch



class QuotesViewModel(
    private val databaseHelper: DatabaseHelper,
    /**
     * QuoteType is in data classes util
     */
    private val quoteType: QuoteType
) : ViewModel() {

    private val quoteDao = QuoteDao(databaseHelper.writableDatabase)
    private val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>> get() = _quotes

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    init {
        loadQuotes()
    }

    fun loadQuotes() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val quotes = when (quoteType) {
                    QuoteType.FAVORITE -> quoteDao.getFavoriteQuotes(AuthManager.currentUserId)
                    QuoteType.USER_MADE -> quoteDao.getUserMadeQuotes(AuthManager.currentUserId)
                }
                _quotes.postValue(quotes)
                _uiState.value = if (quotes.isEmpty()) UiState.Empty else UiState.Success
            } catch (e: Exception) {
                Log.e("QuotesVM", "Error loading $quoteType quotes: ${e.message}")
                _uiState.value = UiState.Error("Failed to load quotes")
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        object Empty : UiState()
        data class Error(val message: String) : UiState()
    }
}