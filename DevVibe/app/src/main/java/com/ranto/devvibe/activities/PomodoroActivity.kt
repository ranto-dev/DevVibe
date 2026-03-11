package com.ranto.devvibe.activities

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat

enum class PomodoroState {
    FOCUS,
    BREAK
}

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
    private var currentState = PomodoroState.FOCUS
    private var focusDuration = 25 * 60 * 1000L
    private var breakDuration = 5 * 60 * 1000L
    private lateinit var stateText: TextView

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {

                if (currentState == PomodoroState.FOCUS) {
                    currentState = PomodoroState.BREAK
                    timeLeftInMillis = breakDuration
                    Toast.makeText(this@PomodoroActivity, "Break time!", Toast.LENGTH_SHORT).show()
                } else {
                    currentState = PomodoroState.FOCUS
                    timeLeftInMillis = focusDuration
                    Toast.makeText(this@PomodoroActivity, "Focus time!", Toast.LENGTH_SHORT).show()
                }

                updateTimer()
                showNotification("Session finished!")
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

        stateText.text = if (currentState == PomodoroState.FOCUS) {
            "FOCUS"
        } else {
            "BREAK"
        }
    }

    private fun showNotification(message: String) {

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "pomodoro_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("DevVibe")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ranto.devvibe.R.layout.activity_pomodoro)

        timerText = findViewById(com.ranto.devvibe.R.id.timerText)
        etMinutes = findViewById(com.ranto.devvibe.R.id.etMinutes)
        btnSetTimer = findViewById(com.ranto.devvibe.R.id.btnSetTimer)
        btnStart = findViewById(com.ranto.devvibe.R.id.btnStart)
        btnPause = findViewById(com.ranto.devvibe.R.id.btnPause)
        btnReset = findViewById(com.ranto.devvibe.R.id.btnReset)
        stateText = findViewById(com.ranto.devvibe.R.id.stateText)

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