package com.android.newuplift.fragments

import QuotesViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.utility.Quote
import com.android.newuplift.utility.QuoteType
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.Locale

class QuoteDetailsFragment : Fragment() {

    private val args: QuoteDetailsFragmentArgs by navArgs()
    private val viewModel: QuotesViewModel by viewModels {
        QuotesViewModelFactory(DatabaseHelper(requireContext()), QuoteType.USER_MADE)
    }
    private lateinit var quote: Quote

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quote_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quote = args.quote

        val quoteTextView: TextView = view.findViewById(R.id.quoteTextView)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val tagsChipGroup: ChipGroup = view.findViewById(R.id.tagsChipGroup)
        val favoriteSwitch: SwitchMaterial = view.findViewById(R.id.favoriteSwitch)
        val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
        val editFab: FloatingActionButton = view.findViewById(R.id.editFab)

        // Populate UI
        quoteTextView.text = quote.quote
        authorTextView.text = quote.author
        favoriteSwitch.isChecked = quote.isFavorite
        favoriteSwitch.isEnabled = false // Toggled only in EditQuoteFragment
        timestampTextView.text = SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
        ).format(quote.timestamp)

        // Populate tags
        tagsChipGroup.removeAllViews()
        quote.tags.forEach { tag ->
            val chip = Chip(context).apply {
                text = tag
                isClickable = false
                isCheckable = false
            }
            tagsChipGroup.addView(chip)
        }

        // Show edit button only for user-made quotes
        editFab.visibility = if (quote.isUserMade) View.VISIBLE else View.GONE
        editFab.setOnClickListener {
            val action = QuoteDetailsFragmentDirections.actionQuoteDetailsToEditQuote(quote)
            findNavController().navigate(action)
        }

        // Handle updated quote from EditQuoteFragment
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Quote>("updatedQuote")
            ?.observe(viewLifecycleOwner) { updatedQuote ->
                quote = updatedQuote
                quoteTextView.text = updatedQuote.quote
                authorTextView.text = updatedQuote.author
                favoriteSwitch.isChecked = updatedQuote.isFavorite
                timestampTextView.text = SimpleDateFormat(
                    "MMM dd, yyyy",
                    Locale.getDefault()
                ).format(updatedQuote.timestamp)
                tagsChipGroup.removeAllViews()
                updatedQuote.tags.forEach { tag ->
                    val chip = Chip(context).apply {
                        text = tag
                        isClickable = false
                        isCheckable = false
                    }
                    tagsChipGroup.addView(chip)
                }
            }
    }
}