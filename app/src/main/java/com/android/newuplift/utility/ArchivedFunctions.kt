package com.android.newuplift.utility

import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue

private fun nextQuote(){
//    if(quoteQueue.isEmpty()){
//        fetchQuoteApi()
//    }
//    val currQuote = quoteQueue.poll() ?: Quote("Please", "click again", "pls")
//    val display = "\"${currQuote.q}\"\n- ${currQuote.a}"
//    quoteTextView.text = display
}

private fun fetchQuoteApi() {
//    viewLifecycleOwner.lifecycleScope.launch {
//        try {
//            val quotes = RetrofitClient.api.getRandomQuote()
//            quoteQueue.addAll(quotes)
//        } catch (e: Exception) {
//            Toast.makeText(context, "Error:${e.message}", Toast.LENGTH_LONG).show()
//        }
//    }
}



//            try {
//                val quote = RetrofitClient.api.getRandomQuote()
//                val display = "\"${quote.content}\"\n- ${quote.author}"
//                quoteTextView.text = display
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
//            }
