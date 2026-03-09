package com.ranto.devvibe
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnPomodoro = findViewById<Button>(R.id.btnPomodoro)
        val btnMusic = findViewById<Button>(R.id.btnMusic)
        val btnStats = findViewById<Button>(R.id.btnStats)
        val btnDailyQuote = findViewById<Button>(R.id.btnDailyQuote)


        btnPomodoro.setOnClickListener {
            startActivity(Intent(this, PomodoroActivity::class.java))
        }

        btnMusic.setOnClickListener {
            startActivity(Intent(this, MusicActivity::class.java))
        }

        btnStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        btnDailyQuote.setOnClickListener {
            startActivity(Intent(this@MainActivity, QuoteActivity::class.java))
        }
    }
}