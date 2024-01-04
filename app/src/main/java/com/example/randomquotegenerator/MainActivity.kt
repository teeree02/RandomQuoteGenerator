package com.example.randomquotegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var quoteTV: TextView
    private lateinit var authorTV: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteTV = findViewById(R.id.quoteTextView)
        authorTV = findViewById(R.id.authorTextView)
        progressBar = findViewById(R.id.progressBar)

        loadRandomQuote()

    }

    private fun loadRandomQuote() {

        quoteTV.text = ""
        authorTV.text = ""
        progressBar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            val quote = fetchRandomQuote()
            displayQuote(quote)
            progressBar.visibility = View.GONE
        }

    }

    private suspend fun fetchRandomQuote(): Quote {
        return try {
            RetrofitInstance.api.getRandomQuote()
        } catch (e: Exception) {
            Quote("Error fetching quote", "Unknown")
        }
    }

    private fun displayQuote(quote: Quote) {
        quoteTV.text = "\"${quote.content}\""
        authorTV.text = "- ${quote.author}"
    }

    fun onNextButtonClick(view: View) {
        loadRandomQuote()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareQuote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareQuote() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${quoteTV.text} ${authorTV.text}")
        startActivity(Intent.createChooser(shareIntent, "Share using"))
    }


}