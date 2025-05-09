package com.android.newuplift.fragments

import QuotesViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.utility.QuoteType
import com.android.newuplift.utility.QuotesAdapter
import com.android.newuplift.utility.showToast

class FavoriteQuotesFragment : Fragment() {

    private val viewModel: QuotesViewModel by viewModels {
        QuotesViewModelFactory(DatabaseHelper(requireContext()), QuoteType.FAVORITE)
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
        recyclerView.setHasFixedSize(false)
        recyclerView.isNestedScrollingEnabled = true

        quotesAdapter = QuotesAdapter(emptyList())
        recyclerView.adapter = quotesAdapter

        // Restore scroll state
        savedRecyclerViewState?.let {
            layoutManager.onRestoreInstanceState(it.getParcelable("layout_state"))
        }

        viewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            Log.d("FavoriteQuotes", "Quotes updated: ${quotes.size} items")
            quotesAdapter.updateQuotes(quotes)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is QuotesViewModel.UiState.Loading -> {
                    Log.d("FavoriteQuotes", "Loading quotes...")
                }
                is QuotesViewModel.UiState.Success -> {
                    showToast("Favorites loaded")
                }
                is QuotesViewModel.UiState.Empty -> {
                    showToast("No favorite quotes yet")
                }
                is QuotesViewModel.UiState.Error -> {
                    showToast(state.message)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val recyclerState = Bundle()
        recyclerState.putParcelable("layout_state", layoutManager.onSaveInstanceState())
        outState.putBundle("recycler_state", recyclerState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteQuotesFragment()
    }
}