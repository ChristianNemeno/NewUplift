package com.android.newuplift.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.utility.QuotesAdapter
import com.android.newuplift.utility.showToast

class FavoriteQuotesFragment : Fragment() {

    private val viewModel: FavoriteQuotesViewModel by viewModels {
        FavoriteQuotesViewModelFactory(DatabaseHelper(requireContext()))
    }
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var quotesAdapter: QuotesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var savedRecyclerViewState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedRecyclerViewState = savedInstanceState?.getBundle("recycler_state")
    }

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
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false) // Allow dynamic height
        recyclerView.isNestedScrollingEnabled = true // Enable scrolling

        val currentQuotes = viewModel.favoriteQuotes.value ?: emptyList()
        quotesAdapter = QuotesAdapter(currentQuotes) { quote, isFavorite ->
            Log.d("FavoriteQuotes", "Toggling quoteId=${quote.id} to isFavorite=$isFavorite")
            viewModel.toggleFavorite(quote, isFavorite)
        }
        recyclerView.adapter = quotesAdapter

        // Restore scroll state
        savedRecyclerViewState?.let {
            layoutManager.onRestoreInstanceState(it.getParcelable("layout_state"))
        }

        viewModel.favoriteQuotes.observe(viewLifecycleOwner) { quotes ->
            Log.d("FavoriteQuotes", "Quotes updated: ${quotes.size} items")
            quotesAdapter.updateQuotes(quotes)
            recyclerView.requestLayout()
            recyclerView.post {
                val heightPx = recyclerView.height
                val heightDp = heightPx / resources.displayMetrics.density
                val orientation = if (heightPx > recyclerView.width) "Portrait" else "Landscape"
                Log.d("FavoriteQuotes", "Orientation: $orientation")
                Log.d("FavoriteQuotes", "RecyclerView height: ${heightPx}px (${heightDp}dp)")
                Log.d("FavoriteQuotes", "Visible item count: ${layoutManager.childCount}")
                Log.d("FavoriteQuotes", "Total item count: ${quotesAdapter.itemCount}")
                for (i in 0 until layoutManager.childCount) {
                    val holder = recyclerView.findViewHolderForAdapterPosition(i)
                    val itemHeightPx = holder?.itemView?.height ?: 0
                    val itemHeightDp = itemHeightPx / resources.displayMetrics.density
                    Log.d("FavoriteQuotes", "Item $i visible: ${holder?.itemView?.isShown}, height: ${itemHeightPx}px (${itemHeightDp}dp)")
                }
                // Ensure scroll to top works
                recyclerView.scrollToPosition(0)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteQuotesViewModel.UiState.Loading -> {
                    Log.d("FavoriteQuotes", "Loading favorites...")
                }
                is FavoriteQuotesViewModel.UiState.Success -> {
                    showToast("Favorites loaded")
                }
                is FavoriteQuotesViewModel.UiState.Empty -> {
                    showToast("No favorite quotes yet")
                }
                is FavoriteQuotesViewModel.UiState.Error -> {
                    showToast(state.message)
                }
            }
        }

        if (savedInstanceState != null) {
            viewModel.loadFavoriteQuotes()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val recyclerState = Bundle()
        recyclerState.putParcelable("layout_state", layoutManager.onSaveInstanceState())
        outState.putBundle("recycler_state", recyclerState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavoriteQuotes()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteQuotesFragment()
    }
}

