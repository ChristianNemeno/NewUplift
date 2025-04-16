package com.android.newuplift.fragments

import QuotesViewModelFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.Quote
import com.android.newuplift.utility.QuoteType
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class EditQuoteFragment : Fragment() {

    private val args: EditQuoteFragmentArgs by navArgs()
    private val viewModel: QuotesViewModel by viewModels {
        QuotesViewModelFactory(DatabaseHelper(requireContext()), QuoteType.USER_MADE)
    }

    private lateinit var quoteEditText: TextInputEditText
    private lateinit var authorTextView: TextView
    private lateinit var tagEditText: TextInputEditText
    private lateinit var tagsChipGroup: ChipGroup
    private lateinit var charCountTextView: TextView
    private lateinit var previewQuoteTextView: TextView
    private lateinit var previewAuthorTextView: TextView
    private lateinit var favoriteSwitch: SwitchMaterial
    private lateinit var addTagButton: Button
    private lateinit var saveQuoteButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_quote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteEditText = view.findViewById(R.id.quoteEditText)
        authorTextView = view.findViewById(R.id.authorTextView)
        tagEditText = view.findViewById(R.id.tagEditText)
        tagsChipGroup = view.findViewById(R.id.tagsChipGroup)
        charCountTextView = view.findViewById(R.id.charCountTextView)
        previewQuoteTextView = view.findViewById(R.id.previewQuoteTextView)
        previewAuthorTextView = view.findViewById(R.id.previewAuthorTextView)
        favoriteSwitch = view.findViewById(R.id.favoriteSwitch)
        addTagButton = view.findViewById(R.id.addTagButton)
        saveQuoteButton = view.findViewById(R.id.saveQuoteButton)

        // Initialize with quote data
        val quote = args.quote
        quoteEditText.setText(quote.quote)
        authorTextView.text = quote.author
        favoriteSwitch.isChecked = quote.isFavorite
        previewQuoteTextView.text = quote.quote
        previewAuthorTextView.text = quote.author
        charCountTextView.text = "Characters: ${quote.quote.length}"
        tagsChipGroup.removeAllViews()
        quote.tags.forEach { tag ->
            addTagToChipGroup(tag, quote.isUserMade)
        }

        // Lock fields for non-user-made quotes
        if (!quote.isUserMade) {
            quoteEditText.isEnabled = false
            tagEditText.isEnabled = false
            addTagButton.isEnabled = false
            tagsChipGroup.isEnabled = false
            for (i in 0 until tagsChipGroup.childCount) {
                tagsChipGroup.getChildAt(i).isEnabled = false
            }
            quoteEditText.hint = "Cannot edit API quote"
        } else {
            quoteEditText.isEnabled = true
            tagEditText.isEnabled = true
            addTagButton.isEnabled = true
            tagsChipGroup.isEnabled = true
        }

        // Author is always locked
        authorTextView.isEnabled = false

        // Real-time updates for user-made quotes
        if (quote.isUserMade) {
            quoteEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val count = s?.length ?: 0
                    charCountTextView.text = "Characters: $count"
                    previewQuoteTextView.text = s?.toString() ?: ""
                }
            })

            addTagButton.setOnClickListener {
                val tag = tagEditText.text.toString().trim()
                if (tag.isNotEmpty() && tag.length <= 20) {
                    addTagToChipGroup(tag, true)
                    tagEditText.text?.clear()
                } else {
                    Toast.makeText(context, "Enter a valid tag (max 20 chars)", Toast.LENGTH_SHORT).show()
                }
            }
        }

        saveQuoteButton.setOnClickListener {
            saveQuote(quote)
        }
    }

    private fun addTagToChipGroup(tag: String, isUserMade: Boolean) {
        for (i in 0 until tagsChipGroup.childCount) {
            val chip = tagsChipGroup.getChildAt(i) as? Chip
            if (chip?.text.toString().equals(tag, ignoreCase = true)) {
                Toast.makeText(context, "Tag already added", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val chip = Chip(context).apply {
            text = tag
            isCloseIconVisible = isUserMade
            isEnabled = isUserMade
            setOnCloseIconClickListener {
                if (isUserMade) {
                    tagsChipGroup.removeView(this)
                }
            }
        }
        tagsChipGroup.addView(chip)
    }

    private fun saveQuote(originalQuote: Quote) {
        val userId = AuthManager.currentUserId
        if (userId == -1) {
            Toast.makeText(context, "Please log in to edit quotes", Toast.LENGTH_SHORT).show()
            return
        }

        if (originalQuote.isUserMade) {
            val quoteText = quoteEditText.text.toString().trim()
            if (quoteText.isEmpty()) {
                Toast.makeText(context, "Quote cannot be empty", Toast.LENGTH_SHORT).show()
                return
            }

            val tags = mutableListOf<String>()
            for (i in 0 until tagsChipGroup.childCount) {
                val chip = tagsChipGroup.getChildAt(i) as? Chip
                chip?.text?.toString()?.let { tags.add(it) }
            }

            val updatedQuote = Quote(
                id = originalQuote.id,
                quote = quoteText,
                author = originalQuote.author,
                timestamp = originalQuote.timestamp,
                tags = tags,
                isFavorite = favoriteSwitch.isChecked,
                isUserMade = true,
                length = quoteText.length
            )
            viewModel.updateQuote(updatedQuote, userId)

            // Pass updated quote back if coming from QuoteDetailsFragment
            findNavController().previousBackStackEntry?.savedStateHandle?.set("updatedQuote", updatedQuote)
        } else {
            // Only update favorite status for non-user-made quotes
            viewModel.toggleFavorite(originalQuote.id, favoriteSwitch.isChecked, userId)
        }

        findNavController().navigateUp()
    }
}