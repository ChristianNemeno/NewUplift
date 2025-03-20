package com.android.newuplift

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesAdapter(
    private var quotes: List<Quote>,
    private val onFavoriteChanged: (Quote, Boolean) -> Unit
) : RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuoteContent: TextView = itemView.findViewById(R.id.tvQuoteContent)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val cbFavorite: CheckBox = itemView.findViewById(R.id.cbFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quote_item, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]

        // Set quote data
        holder.tvQuoteContent.text = quote.quote
        holder.tvAuthor.text = quote.author ?: "Unknown"

        // Prevent listener from triggering during binding
        holder.cbFavorite.setOnCheckedChangeListener(null)
        holder.cbFavorite.isChecked = quote.isFavorite

        // Set listener after binding to avoid recursive updates
        holder.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
            if (quote.isFavorite != isChecked) { // Only trigger if state changed
                quote.isFavorite = isChecked // Update local object
                onFavoriteChanged(quote, isChecked) // Notify callback
            }
        }
    }

    override fun getItemCount() = quotes.size

    fun updateQuotes(newQuotes: List<Quote>) {
        quotes = newQuotes
        notifyDataSetChanged() // Refresh entire list
    }
}