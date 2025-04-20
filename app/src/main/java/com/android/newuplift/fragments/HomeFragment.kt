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
import androidx.fragment.app.viewModels // Import viewModels delegate
import androidx.lifecycle.Observer // Import Observer
import androidx.navigation.navGraphViewModels
import com.android.newuplift.utility.Quote
import com.android.newuplift.R
// Remove unused RetrofitClient import if fetch is only in ViewModel
// import com.android.newuplift.api.RetrofitClient
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.AuthManager
import com.android.newuplift.utility.pickMood
import com.android.newuplift.viewmodels.HomeViewModel
// Remove unused lifecycleScope and launch imports
// import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var dayTextView: TextView
    private lateinit var buttonGenerateQuote: Button
    private lateinit var homeSpinner: Spinner
    private lateinit var heartButton: ImageButton
    private lateinit var quoteDatabase: DatabaseHelper
    private lateinit var quoteDao: QuoteDao
    private var currentUserId: Int = -1

    private val viewModel: HomeViewModel by navGraphViewModels(R.id.my_nav)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.currentQuote.value == null) {
            val initialTag = pickMood("Happy") // Default mood
            viewModel.fetchInitialQuoteIfNeeded(initialTag)
        }
    }

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
        quoteTextView = view.findViewById(R.id.home_quote_view)
        authorTextView = view.findViewById(R.id.author_quote_view)
        buttonGenerateQuote = view.findViewById(R.id.home_generate_button)
        homeSpinner = view.findViewById(R.id.home_spinner)
        heartButton = view.findViewById(R.id.btn_heart)

        quoteDatabase = DatabaseHelper(requireContext())
        quoteDao = QuoteDao(quoteDatabase.writableDatabase)
        currentUserId = AuthManager.currentUserId

        val (formattedDate, formattedDay) = getCurrentFormattedDate()
        dateTextView.text = formattedDate
        dayTextView.text = formattedDay

        val adapter = ArrayAdapter(
            view.context,
            R.layout.home_spinner_item,
            arrayOf("Happy", "Lost", "Motivated", "Bored")
        )
        adapter.setDropDownViewResource(R.layout.home_spinner_dropdown_item)
        homeSpinner.adapter = adapter

        viewModel.currentQuote.observe(viewLifecycleOwner, Observer { quote ->
            if (quote != null) {
                val content = quote.quote
                val author = quote.author
                val display = "\"$content\""
                quoteTextView.text = display
                authorTextView.text = "- $author"
                updateHeartButtonState(quote)
            } else {
                quoteTextView.text = getString(R.string.default_quote_text)
                authorTextView.text = ""
                heartButton.isSelected = false
                heartButton.setImageResource(R.drawable.home_ic_heart)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            buttonGenerateQuote.isEnabled = !isLoading
        })

        // Button click listeners
        buttonGenerateQuote.setOnClickListener {
            val selectedMood = homeSpinner.selectedItem?.toString() ?: "Happy"
            val tag = pickMood(selectedMood)
            viewModel.fetchNewQuote(tag)
            heartButton.isSelected = false
            heartButton.setImageResource(R.drawable.home_ic_heart)
        }

        heartButton.setOnClickListener {
            val displayedQuote = viewModel.currentQuote.value
            displayedQuote?.let { quote ->
                handleFavoriteToggle(quote)
            } ?: run {
                heartButton.isSelected = false
                heartButton.setImageResource(R.drawable.home_ic_heart)
                Toast.makeText(context, "No quote to favorite", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleFavoriteToggle(quote: Quote) {
        if (currentUserId == -1) {
            Toast.makeText(context, "Please log in to favorite quotes", Toast.LENGTH_SHORT).show()
            return
        }

        val quoteId = quote.id
        val exists = quoteDao.isQuoteExists(quoteId, currentUserId)

        if (exists) {
            // Quote exists in DB, toggle its favorite status
            val currentDbFavoriteStatus = quoteDao.getFavoriteStatus(quoteId, currentUserId) // Need to add this method to DAO
            val newFavoriteStatus = !currentDbFavoriteStatus
            val rowsUpdated = quoteDao.updateFavorite(quoteId, currentUserId, newFavoriteStatus)

            if (rowsUpdated > 0) {
                heartButton.isSelected = newFavoriteStatus
                heartButton.setImageResource(
                    if (newFavoriteStatus) R.drawable.home_ic_heart_filled
                    else R.drawable.home_ic_heart
                )
                // Update the quote in the ViewModel to reflect the change immediately (optional, DB is source of truth)
                //viewModel.updateQuoteFavoriteStatus(quoteId, newFavoriteStatus)
                Toast.makeText(
                    context,
                    if (newFavoriteStatus) "Added to favorites" else "Removed from favorites",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Failed to update favorite status", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Quote doesn't exist in DB, insert it as a favorite
            val quoteToInsert = quote.copy(isFavorite = true) // Ensure it's marked as favorite
            val newRowId = quoteDao.insertQuote(quoteToInsert, currentUserId)
            if (newRowId != -1L) {
                heartButton.isSelected = true
                heartButton.setImageResource(R.drawable.home_ic_heart_filled)
                // Update the quote in the ViewModel (optional)
                //viewModel.updateQuoteFavoriteStatus(quoteId, true)
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                // Handle potential insertion failure (e.g., unique constraint if quote already exists somehow)
                Toast.makeText(context, "Failed to add favorite (already exists or error)", Toast.LENGTH_SHORT).show()
                // Re-check status and update button
                updateHeartButtonState(quote)
            }
        }
    }

    // Helper to update heart button based on quote data and DB status
    private fun updateHeartButtonState(quote: Quote) {
        if (currentUserId != -1) {
            val isFavoriteInDb = quoteDao.isQuoteFavorite(quote.id, currentUserId) // Need to add this method to DAO
            heartButton.isSelected = isFavoriteInDb
            heartButton.setImageResource(
                if (isFavoriteInDb) R.drawable.home_ic_heart_filled
                else R.drawable.home_ic_heart
            )
        } else {
            // Not logged in, cannot be favorite
            heartButton.isSelected = false
            heartButton.setImageResource(R.drawable.home_ic_heart)
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

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}