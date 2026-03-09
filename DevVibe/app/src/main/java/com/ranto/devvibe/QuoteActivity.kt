package com.ranto.devvibe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class QuoteActivity : AppCompatActivity() {
    private lateinit var quoteText: TextView
    private lateinit var btnNewQuote: Button

    private val quotesList = listOf(
        "First, solve the problem. Then, write the code. — John Johnson",
        "Programs must be written for people to read. — Harold Abelson",
        "Code is like humor. When you have to explain it, it’s bad. — Cory House",
        "Simplicity is the soul of efficiency. — Austin Freeman",
        "Debugging is like being the detective in a crime movie where you are also the murderer. — Filipe Fortes"
    )

    private fun showRandomQuote() {
        val randomIndex = Random.nextInt(quotesList.size)
        quoteText.text = quotesList[randomIndex]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quote)


        quoteText = findViewById(R.id.quoteText)
        btnNewQuote = findViewById(R.id.btnNewQuote)

        showRandomQuote()

        btnNewQuote.setOnClickListener {
            showRandomQuote()
        }
    }
}