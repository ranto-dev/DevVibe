package com.ranto.devvibe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class PomodoroActivity : AppCompatActivity() {
    private lateinit var timerText: TextView
    private lateinit var etMinutes: EditText
    private lateinit var btnSetTimer: Button
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timeLeftInMillis: Long = 1500000 // 25 minutes
    private var timer: CountDownTimer? = null
    private var timerRunning = false

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                timerRunning = false
            }
        }.start()

        timerRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        timerRunning = false
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = 1500000
        updateTimer()
    }

    @SuppressLint("DefaultLocale")
    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        timerText.text = timeFormatted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pomodoro)

        timerText = findViewById(R.id.timerText)
        etMinutes = findViewById(R.id.etMinutes)
        btnSetTimer = findViewById(R.id.btnSetTimer)
        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)

        btnSetTimer.setOnClickListener {
            val input = etMinutes.text.toString().trim()
            if (input.isNotEmpty()) {
                try {
                    val minutes = input.toLong()
                    if (minutes > 0) {
                        timeLeftInMillis = minutes * 60 * 1000 // convertir en millisecondes
                        updateTimer()
                        etMinutes.text.clear()
                    } else {
                        Toast.makeText(this, "Entrez un nombre supérieur à 0", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Valeur invalide", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Veuillez entrer une durée", Toast.LENGTH_SHORT).show()
            }
        }
        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }
}