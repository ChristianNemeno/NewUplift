package com.android.newuplift.fragments

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
import androidx.navigation.fragment.findNavController
import com.android.newuplift.R
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.Quote
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class MakeQuotesFragment : Fragment() {

    private lateinit var quoteDao: QuoteDao
    private lateinit var quoteEditText: TextInputEditText
    private lateinit var authorEditText: TextInputEditText
    private lateinit var tagEditText: TextInputEditText
    private lateinit var tagsChipGroup: ChipGroup
    private lateinit var charCountTextView: TextView
    private lateinit var previewQuoteTextView: TextView
    private lateinit var previewAuthorTextView: TextView
    private lateinit var favoriteSwitch: SwitchMaterial
    private lateinit var addTagButton: Button
    private lateinit var saveQuoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(dbHelper.writableDatabase)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_make_quotes, container, false)

        quoteEditText = view.findViewById(R.id.quoteEditText)
        authorEditText = view.findViewById(R.id.authorEditText)
        tagEditText = view.findViewById(R.id.tagEditText)
        tagsChipGroup = view.findViewById(R.id.tagsChipGroup)
        charCountTextView = view.findViewById(R.id.charCountTextView)
        previewQuoteTextView = view.findViewById(R.id.previewQuoteTextView)
        previewAuthorTextView = view.findViewById(R.id.previewAuthorTextView)
        favoriteSwitch = view.findViewById(R.id.favoriteSwitch)
        addTagButton = view.findViewById(R.id.addTagButton)
        saveQuoteButton = view.findViewById(R.id.saveQuoteButton)

        quoteEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val count = s?.length ?: 0
                charCountTextView.text = "Characters: $count"
                previewQuoteTextView.text = s?.toString() ?: ""
            }
        })

        authorEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                previewAuthorTextView.text = s?.toString()?.takeIf { it.isNotEmpty() } ?: "Anonymous"
            }
        })

        addTagButton.setOnClickListener {
            val tag = tagEditText.text.toString().trim()
            if (tag.isNotEmpty() && tag.length <= 20) {
                addTagToChipGroup(tag)
                tagEditText.text?.clear()
            } else {
                Toast.makeText(context, "Enter a valid tag (max 20 chars)", Toast.LENGTH_SHORT).show()
            }
        }

        saveQuoteButton.setOnClickListener {
            saveQuote()
        }

        return view
    }

    /**
     * A utility function to add a tag , using chip group
     * usage :
     * line 83
     */
    private fun addTagToChipGroup(tag: String) {
        for (i in 0 until tagsChipGroup.childCount) {
            val chip = tagsChipGroup.getChildAt(i) as? Chip
            if (chip?.text.toString().equals(tag, ignoreCase = true)) {
                Toast.makeText(context, "Tag already added", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val chip = Chip(context).apply {
            text = tag
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                tagsChipGroup.removeView(this)
            }
        }
        tagsChipGroup.addView(chip)
    }

    /**
     * The save quote function , well this function
     * self explanatory
     */
    private fun saveQuote() {
        val quoteText = quoteEditText.text.toString().trim()
        val author = authorEditText.text.toString().trim()
        val isFavorite = favoriteSwitch.isChecked

        if (quoteText.isEmpty()) {
            Toast.makeText(context, "Quote cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = AuthManager.currentUserId
        if (userId == -1) {
            Toast.makeText(context, "Please log in to save quotes", Toast.LENGTH_SHORT).show()
            return
        }

        val tags = mutableListOf<String>()
        for (i in 0 until tagsChipGroup.childCount) {
            val chip = tagsChipGroup.getChildAt(i) as? Chip
            chip?.text?.toString()?.let { tags.add(it) }
        }
        /**
         * kani na part is mag himo siyag code fetchign the details sa swithc if favorites
         * but naka default nang isUserMade
         */
        val quote = Quote(
            id = UUID.randomUUID().hashCode(),
            quote = quoteText,
            author = author.ifEmpty { "Anonymous" },
            timestamp = System.currentTimeMillis(),
            tags = tags,
            isFavorite = isFavorite,
            isUserMade = true,
            length = quoteText.length
        )


        /**
         * kani mu interact sa dp
         * -1L means failed ,quote exists iirc
         *
         */
        try {
            val result = quoteDao.insertQuote(quote, userId)
            if (result != -1L) {
                Toast.makeText(context, "Quote saved!", Toast.LENGTH_SHORT).show()
                quoteEditText.text?.clear()
                authorEditText.text?.clear()
                tagsChipGroup.removeAllViews()
                favoriteSwitch.isChecked = false
                previewQuoteTextView.text = ""
                previewAuthorTextView.text = ""
                charCountTextView.text = "Characters: 0"
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Quote already exists", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving quote: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}