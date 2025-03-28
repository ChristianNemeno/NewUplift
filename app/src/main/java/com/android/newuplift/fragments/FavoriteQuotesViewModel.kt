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
import kotlinx.coroutines.launch

class FavoriteQuotesViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    private val quoteDao = QuoteDao(databaseHelper.writableDatabase)
    private val _favoriteQuotes = MutableLiveData<List<Quote>>()
    val favoriteQuotes: LiveData<List<Quote>> get() = _favoriteQuotes

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    init {
        loadFavoriteQuotes()
    }

    fun loadFavoriteQuotes() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val quotes = quoteDao.getFavoriteQuotes(AuthManager.currentUserId)
                _favoriteQuotes.postValue(quotes)
                _uiState.value = if (quotes.isEmpty()) UiState.Empty else UiState.Success
            } catch (e: Exception) {
                Log.e("FavoriteQuotesVM", "Error loading quotes: ${e.message}")
                _uiState.value = UiState.Error("Failed to load favorites")
            }
        }
    }

    fun toggleFavorite(quote: Quote, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                val rowsUpdated = quoteDao.updateFavorite(quote.id, AuthManager.currentUserId, isFavorite)
                if (rowsUpdated > 0) {
                    Log.d("FavoriteQuotesVM", "Updated quoteId=${quote.id} to isFavorite=$isFavorite")
                    loadFavoriteQuotes() // Refresh the list
                } else {
                    Log.w("FavoriteQuotesVM", "No rows updated for quoteId=${quote.id}")
                    _uiState.value = UiState.Error("Failed to update favorite")
                }
            } catch (e: Exception) {
                Log.e("FavoriteQuotesVM", "Error updating favorite: ${e.message}")
                _uiState.value = UiState.Error("Failed to update favorite")
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