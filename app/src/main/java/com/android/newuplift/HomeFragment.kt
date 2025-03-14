package com.android.newuplift

import android.graphics.drawable.Animatable
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch



class HomeFragment : Fragment() {
    private var tag : String = "Happy"
    private lateinit var quoteTextView : TextView
    private lateinit var buttonGenerateQuote : Button
    private lateinit var homeSpinner : Spinner
    private lateinit var choiceSpinner : String
    //private lateinit var quote : String
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

        // to use this just assign the current choice
        // e.g spinner.selectedItem?.toString() ?: "None"
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            arrayOf("Happy", "Lost", "Motivated", "Bored")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // This was missing
        homeSpinner.adapter = adapter


        // break!
        buttonGenerateQuote.setOnClickListener {
            // default would be Happy if walay gipili
            choiceSpinner  = homeSpinner.selectedItem?.toString() ?: "Happy"
            // this is an util function abstracted naas util.kt basically maps the mood sa tags
            tag = pickMood(choiceSpinner)

            fetchQuoteApi()

        }

    }

    private fun fetchQuoteApi() {
        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val quotes = RetrofitClient.api.getRandomQuote(tag)
                if(quotes.isNotEmpty()){
                    val quote = quotes[0]
                    val content = quote.content
                    val author = quote.author
                    val tagsDisplay = quote.tags.joinToString(", ")
                    val display = "\"$content\"\n- $author\n\n Tags: $tagsDisplay"
                    quoteTextView.text = display
                }else{
                    quoteTextView.text = "Please try again"
                    Toast.makeText(context, "No quotes available for '$tag'", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception) {
                e.printStackTrace()
                quoteTextView.text = "Error: ${e.message}"
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun startFlameAnimation(view: View) {
        val flameImageView = view.findViewById<ImageView>(R.id.my_image_view)
        val drawable = flameImageView?.drawable
        if (drawable is Animatable) {
            drawable.start()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}

