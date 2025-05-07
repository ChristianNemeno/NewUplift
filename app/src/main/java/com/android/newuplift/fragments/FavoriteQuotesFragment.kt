package com.android.newuplift.fragments

import com.android.newuplift.viewmodels.QuotesViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.utility.QuoteType
import com.android.newuplift.utility.QuotesAdapter
import com.android.newuplift.utility.showToast
import com.android.newuplift.viewmodels.QuotesViewModel

class FavoriteQuotesFragment : Fragment() {

    private val viewModel: QuotesViewModel by viewModels {
        QuotesViewModelFactory(DatabaseHelper(requireContext()), QuoteType.FAVORITE)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var quotesAdapter: QuotesAdapter
    private lateinit var emptyStateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_quotes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewQuotes)
        emptyStateTextView = view.findViewById(R.id.emptyStateText)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.isNestedScrollingEnabled = true

        quotesAdapter = QuotesAdapter(
            emptyList(),
            onQuoteClick = { quote ->
                val action = FavoriteQuotesFragmentDirections.actionFavoriteQuotesToEditQuote(quote)
                findNavController().navigate(action)
            }
        )
        recyclerView.adapter = quotesAdapter

        viewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            quotesAdapter.updateQuotes(quotes)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is QuotesViewModel.UiState.Loading -> {
                    emptyStateTextView.visibility = View.GONE
                }
                is QuotesViewModel.UiState.Success -> {
                    emptyStateTextView.visibility = View.GONE
                    //showToast("Favorite quotes loaded")
                }
                is QuotesViewModel.UiState.Empty -> {
                    emptyStateTextView.visibility = View.VISIBLE
                }
                is QuotesViewModel.UiState.Error -> {
                    emptyStateTextView.visibility = View.GONE
                    showToast(state.message)
                }
                is QuotesViewModel.UiState.UpdateSuccess -> {
                    // No action needed
                }
                is QuotesViewModel.UiState.UpdateError -> {
                    // No action needed
                }
                is QuotesViewModel.UiState.FavoriteToggled -> {
                    showToast("Favorite status updated")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadQuotes()
    }
}