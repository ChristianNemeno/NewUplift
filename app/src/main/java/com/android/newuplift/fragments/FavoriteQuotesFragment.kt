package com.android.newuplift.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newuplift.utility.QuotesAdapter
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.showToast
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

        recyclerView = view.findViewById(R.id.recyclerViewQuotes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        quoteDatabase = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)


        quotesAdapter = QuotesAdapter(emptyList()) { quote, isFavorite ->
            Log.d("FavoriteQuotes", "Toggling: quoteId=${quote.id}, userId=${AuthManager.currentUserId}, isFavorite=$isFavorite")
            viewLifecycleOwner.lifecycleScope.launch {
                val rowsUpdated = quoteDao.updateFavorite(quote.id, AuthManager.currentUserId, isFavorite)// change this!
                Log.d("FavoriteQuotes", "Update result: rowsUpdated=$rowsUpdated, quoteId=${quote.id}, userId=${AuthManager.currentUserId}")
                if (rowsUpdated > 0) {
                    showToast("This is the current ID : ${AuthManager.currentUserId}")
                    loadFavoriteQuotes()
                }else{
                    Log.d("FavoriteQuotes", "Update failed - no rows matched")
                }
            }
        }
        recyclerView.adapter = quotesAdapter


        loadFavoriteQuotes()
    }

    private fun loadFavoriteQuotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            val favoriteQuotes = quoteDao.getFavoriteQuotes(AuthManager.currentUserId) // Fetch from DAO
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