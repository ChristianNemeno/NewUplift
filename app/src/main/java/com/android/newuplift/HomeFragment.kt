package com.android.newuplift

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Fix the bug of heart favorite the unheart should unfavorite not add it again in the db
 * it creates duplicates kasi
 */

class HomeFragment : Fragment() {
    private var tag : String = "Happy"
    private lateinit var quoteTextView : TextView
    private lateinit var buttonGenerateQuote : Button
    private lateinit var homeSpinner : Spinner
    private lateinit var choiceSpinner : String
    private lateinit var heart_button : ImageButton
    private var currentQuote: Quote? = null
    private lateinit var quoteDatabase: DatabaseHelper
    private lateinit var quoteDao: QuoteDao


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteTextView = view.findViewById(R.id.home_quote_view)
        buttonGenerateQuote = view.findViewById(R.id.home_generate_button)
        homeSpinner = view.findViewById(R.id.home_spinner)
        heart_button = view.findViewById<ImageButton>(R.id.btn_heart)

        quoteDatabase = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)

        // Spinner setup for mood selection
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            arrayOf("Happy", "Lost", "Motivated", "Bored")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        homeSpinner.adapter = adapter

        buttonGenerateQuote.setOnClickListener {
            choiceSpinner = homeSpinner.selectedItem?.toString() ?: "Happy"
            tag = pickMood(choiceSpinner)
            fetchQuoteApi()
            heart_button.isSelected = false
            heart_button.setImageResource(R.drawable.home_ic_heart)
        }

        heart_button.setOnClickListener {
            it.isSelected = !it.isSelected

            currentQuote?.let { quote ->
                val quoteId = quote.id
                val exists = quoteDao.isQuoteExists(quoteId)

                if (exists) {

                    val rowsUpdated = quoteDao.updateFavorite(quoteId, it.isSelected)
                    if (rowsUpdated > 0) {
                        if (it.isSelected) {
                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                            heart_button.setImageResource(R.drawable.home_ic_heart_filled)
                        } else {
                            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            heart_button.setImageResource(R.drawable.home_ic_heart)
                        }
                    } else {
                        it.isSelected = !it.isSelected
                        heart_button.setImageResource(
                            if (it.isSelected) R.drawable.home_ic_heart_filled else R.drawable.home_ic_heart
                        )
                        Toast.makeText(context, "Failed to update favorite", Toast.LENGTH_SHORT).show()
                    }
                } else if (it.isSelected) {

                    val updatedQuote = quote.copy(isFavorite = true)
                    val newId = quoteDao.insertQuote(updatedQuote)
                    if (newId != -1L) {
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                        heart_button.setImageResource(R.drawable.home_ic_heart_filled)
                    } else {
                        it.isSelected = false
                        heart_button.setImageResource(R.drawable.home_ic_heart)
                        Toast.makeText(context, "Failed to add favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {

                    heart_button.setImageResource(R.drawable.home_ic_heart)
                }
            } ?: run {
                it.isSelected = false
                heart_button.setImageResource(R.drawable.home_ic_heart)
                Toast.makeText(context, "No quote to favorite", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     *  This is a helper function to save everytime i generate , useful for other functionalities,
     *  e.g history if ever mag add ko :-)
     */
    private fun saveQuote() {
        currentQuote?.let { param ->
            val savedQuote = Quote(
                quote = param.quote,
                author = param.author,
                timestamp = System.currentTimeMillis(),
                id = param.id,
                tags = param.tags,
                isFavorite = param.isFavorite,
                isUserMade = param.isUserMade,
                length = param.length
            )
            val newId = quoteDao.insertQuote(savedQuote)
            if (newId != -1L) {
                Toast.makeText(context, "Quote saved !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to save quote", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "No quote to save", Toast.LENGTH_SHORT).show()
        }
    }



    /**
     * nothing to change here
     */
    private fun fetchQuoteApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val quote = RetrofitClient.api.getRandomQuote(tag) // Single Quote object
                currentQuote = quote // Store the quote
                val content = quote.quote // Use "quote" from QuoteSlate
                val author = quote.author
                val tagsDisplay = quote.tags.joinToString(", ")
                val display = "\"$content\"\n- $author\n\nTags: $tagsDisplay"
                quoteTextView.text = display
                saveQuote()
            } catch (e: Exception) {
                e.printStackTrace()
                currentQuote = null // Clear on error
                quoteTextView.text = "Error: ${e.message}"
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}

