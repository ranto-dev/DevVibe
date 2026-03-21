package com.ranto.devvibe.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import kotlin.random.Random
import androidx.activity.enableEdgeToEdge

class QuoteActivity : AppCompatActivity() {
    private lateinit var quoteText: TextView
    private lateinit var btnNewQuote: MaterialButton

    private val quotesList = listOf(
        "First, solve the problem. Then, write the code. — John Johnson",
        "Programs must be written for people to read. — Harold Abelson",
        "Code is like humor. When you have to explain it, it’s bad. — Cory House",
        "Simplicity is the soul of efficiency. — Austin Freeman",
        "Debugging is like being the detective in a crime movie where you are also the murderer. — Filipe Fortes",
        "Before software can be reusable it first has to be usable. — Ralph Johnson",
        "Talk is cheap. Show me the code. — Linus Torvalds",
        "Experience is the name everyone gives to their mistakes. — Oscar Wilde",
        "Make it work, make it right, make it fast. — Kent Beck",
        "The best error message is the one that never shows up. — Thomas Fuchs",
        "Any fool can write code that a computer can understand. Good programmers write code that humans can understand. — Martin Fowler",
        "Good code is its own best documentation. — Steve McConnell",
        "Programs are meant to be read by humans and only incidentally for computers to execute. — Donald Knuth",
        "Programming isn’t about what you know; it’s about what you can figure out. — Chris Pine",
        "Optimism is an occupational hazard of programming: feedback is the treatment. — Kent Beck",
        "Code never lies, comments sometimes do. — Ron Jeffries",
        "Programming is like writing a book... except if you miss out a single comma on page 126 the whole thing makes no sense. — Unknown",
        "The only way to learn a new programming language is by writing programs in it. — Dennis Ritchie",
        "Walking on water and developing software from a specification are easy if both are frozen. — Edward V. Berard",
        "Deleted code is debugged code. — Jeff Sickel",
        "First, solve the problem. Then, write the code. — John Johnson",
        "In order to be irreplaceable, one must always be different. — Coco Chanel",
        "Software is a great combination between artistry and engineering. — Bill Gates",
        "Programming is thinking, not typing. — Casey Patton",
        "Your most unhappy customers are your greatest source of learning. — Bill Gates"
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