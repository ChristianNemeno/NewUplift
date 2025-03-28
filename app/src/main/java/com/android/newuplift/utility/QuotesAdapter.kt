package com.android.newuplift.utility

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.newuplift.R

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

        holder.tvQuoteContent.text = quote.quote
        holder.tvAuthor.text = quote.author ?: "Unknown"

        holder.cbFavorite.setOnCheckedChangeListener(null)
        holder.cbFavorite.isChecked = quote.isFavorite

        holder.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
            Log.d("QuotesAdapter", "Step 2: Checkbox toggled for quoteId=${quote.id}, isChecked=$isChecked")
            if (quote.isFavorite != isChecked) {
                quote.isFavorite = isChecked
                onFavoriteChanged(quote, isChecked)
            }
        }
        holder.cbFavorite.setOnClickListener {
            Log.d("QuotesAdapter", "Step 3: Checkbox clicked for quoteId=${quote.id}")
        }
        Log.d("QuotesAdapter", "Step 4: Binding quoteId=${quote.id}, isFavorite=${quote.isFavorite}")
    }

    override fun getItemCount() = quotes.size
    //hgcffty

    fun updateQuotes(newQuotes: List<Quote>) {
        quotes = newQuotes
        notifyDataSetChanged()
    }
}