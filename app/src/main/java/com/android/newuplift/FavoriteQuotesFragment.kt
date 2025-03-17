package com.android.newuplift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FavoriteQuotesFragment : Fragment() {

    private lateinit var quotesAdapter: QuotesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var quoteDatabase: DatabaseHelper
    private lateinit var quoteDao: QuoteDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_quotes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewQuotes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Database and DAO
        quoteDatabase = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)

        // Initialize Adapter with empty list
        quotesAdapter = QuotesAdapter(emptyList()) { quote, isFavorite ->
            // Handle favorite state change
            viewLifecycleOwner.lifecycleScope.launch {
                val rowsUpdated = quoteDao.updateFavorite(quote.id, isFavorite)
                if (rowsUpdated > 0) {
                    loadFavoriteQuotes() // Refresh list after update
                }
            }
        }
        recyclerView.adapter = quotesAdapter

        // Load favorite quotes
        loadFavoriteQuotes()
    }

    private fun loadFavoriteQuotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            val favoriteQuotes = quoteDao.getFavoriteQuotes() // Fetch from DAO
            quotesAdapter.updateQuotes(favoriteQuotes)
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteQuotes() // Refresh list when returning to fragment
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteQuotesFragment()
    }
}