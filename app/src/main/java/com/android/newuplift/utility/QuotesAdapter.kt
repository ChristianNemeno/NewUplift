package com.android.newuplift.utility

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.newuplift.R

class QuotesAdapter(
    private var quotes: List<Quote>,
    private val onQuoteClicked: (Quote) -> Unit = { _ -> }
) : RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuoteContent: TextView = itemView.findViewById(R.id.tvQuoteContent)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
    }

    override fun getItemViewType(position: Int): Int {
        return if (quotes[position].isUserMade) R.layout.quote_item_user_made else R.layout.quote_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]
        holder.tvQuoteContent.text = quote.quote
        holder.tvAuthor.text = quote.author ?: "Unknown"
        holder.itemView.setOnClickListener {
            onQuoteClicked(quote)
        }
        Log.d("QuotesAdapter", "Binding quoteId=${quote.id}, isUserMade=${quote.isUserMade}")
    }

    override fun getItemCount() = quotes.size
    //hgcffty

    fun updateQuotes(newQuotes: List<Quote>) {
        quotes = newQuotes
        notifyDataSetChanged()
    }
}