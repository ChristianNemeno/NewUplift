package com.android.newuplift.fragments

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
import com.android.newuplift.utility.Quote
import com.android.newuplift.R
import com.android.newuplift.api.RetrofitClient
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.pickMood
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Fix the bug of heart favorite the unheart should unfavorite not add it again in the db
 * it creates duplicates kasi
 */

class HomeFragment : Fragment() {
    private var tag: String = "Happy"
    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var dayTextView: TextView
    private lateinit var buttonGenerateQuote: Button
    private lateinit var homeSpinner: Spinner
    private lateinit var choiceSpinner: String
    private lateinit var heartButton: ImageButton
    private var currentQuote: Quote? = null
    private lateinit var quoteDatabase: DatabaseHelper
    private lateinit var quoteDao: QuoteDao
    private var currentUserId: Int = -1 // You'll need to set this based on your auth system

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateTextView = view.findViewById(R.id.date_MM_DD_YYYY)
        dayTextView = view.findViewById(R.id.date_day)

        val(formattedDate, formattedDay) = getCurrentFormattedDate()
        dateTextView.text = formattedDate
        dayTextView.text = formattedDay

        quoteTextView = view.findViewById(R.id.home_quote_view)
        authorTextView = view.findViewById(R.id.author_quote_view)

        buttonGenerateQuote = view.findViewById(R.id.home_generate_button)
        homeSpinner = view.findViewById(R.id.home_spinner)
        heartButton = view.findViewById(R.id.btn_heart)

        quoteDatabase = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)


        currentUserId = AuthManager.currentUserId

        choiceSpinner = homeSpinner.selectedItem?.toString() ?: "Happy"
        tag = pickMood(choiceSpinner)
        fetchQuoteApi()



        val adapter = ArrayAdapter(
            view.context,
            R.layout.home_spinner_item,
            arrayOf("Happy", "Lost", "Motivated", "Bored")
        )
        adapter.setDropDownViewResource(R.layout.home_spinner_dropdown_item)
        homeSpinner.adapter = adapter

        buttonGenerateQuote.setOnClickListener {
            choiceSpinner = homeSpinner.selectedItem?.toString() ?: "Happy"
            tag = pickMood(choiceSpinner)
            fetchQuoteApi()
            heartButton.isSelected = false
            heartButton.setImageResource(R.drawable.home_ic_heart)
        }

        heartButton.setOnClickListener {
            currentQuote?.let { quote ->
                if (currentUserId == -1) {
                    Toast.makeText(context, "Please log in to favorite quotes", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val quoteId = quote.id
                val exists = quoteDao.isQuoteExists(quoteId, currentUserId)

                if (exists) {

                    val newFavoriteStatus = !heartButton.isSelected
                    val rowsUpdated = quoteDao.updateFavorite(quoteId, currentUserId, newFavoriteStatus)

                    if (rowsUpdated > 0) {
                        heartButton.isSelected = newFavoriteStatus
                        heartButton.setImageResource(
                            if (newFavoriteStatus) R.drawable.home_ic_heart_filled
                            else R.drawable.home_ic_heart
                        )
                        Toast.makeText(
                            context,
                            if (newFavoriteStatus) "Added to favorites" else "Removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Failed to update favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {

                    if (!heartButton.isSelected) {
                        val updatedQuote = quote.copy(isFavorite = true)
                        val newId = quoteDao.insertQuote(updatedQuote, currentUserId)
                        if (newId != -1L) {
                            heartButton.isSelected = true
                            heartButton.setImageResource(R.drawable.home_ic_heart_filled)
                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to add favorite", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } ?: run {
                heartButton.isSelected = false
                heartButton.setImageResource(R.drawable.home_ic_heart)
                Toast.makeText(context, "No quote to favorite", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveQuote() {
        currentQuote?.let { param ->
            if (currentUserId == -1) {
                Toast.makeText(context, "Please log in to save quotes", Toast.LENGTH_SHORT).show()
                return
            }

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
            val newId = quoteDao.insertQuote(savedQuote, currentUserId)
            if (newId != -1L) {
                Toast.makeText(context, "Quote saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Quote already exists", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "No quote to save", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentFormattedDate(): Pair<String, String>{
        val calendar = Calendar.getInstance()

        val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())

        val formattedDate = dateFormatter.format(calendar.time)
        val formattedDay = dayFormatter.format(calendar.time)

        return Pair(formattedDate, formattedDay)
    }

    private fun fetchQuoteApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val quote = RetrofitClient.api.getRandomQuote(tag)
                currentQuote = quote
                val content = quote.quote
                val author = quote.author
                val tagsDisplay = quote.tags.joinToString(", ")
//                val display = "\"$content\" \n\nTags: $tagsDisplay"
                val display = "\"$content\""
                quoteTextView.text = display
                authorTextView.text = "- $author"
                saveQuote()
            } catch (e: Exception) {
                e.printStackTrace()
                currentQuote = null
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