package com.ranto.devvibe.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage

class TimerActivity : AppCompatActivity() {
    private lateinit var taskTitle: TextView
    private lateinit var timerText: TextView

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button
    private lateinit var btnQuit: Button

    private lateinit var countDownTimer: CountDownTimer

    private var totalTime: Long = 0
    private var timeLeft: Long = 0
    private var running = false

    private var taskIndex = -1
    private var tasks = mutableListOf<Task>()

    private fun startTimer() {

        countDownTimer = object : CountDownTimer(timeLeft, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished
                updateTimer()

            }

            override fun onFinish() {

                tasks[taskIndex].isFinished = true

                JsonStorage.saveTasks(this@TimerActivity, tasks)

                Toast.makeText(
                    this@TimerActivity,
                    "Tâche terminée",
                    Toast.LENGTH_LONG
                ).show()

                finish()

            }

        }.start()

        running = true
    }

    private fun pauseTimer() {

        if (running) {

            countDownTimer.cancel()
            running = false

        }
    }

    private fun updateTimer() {

        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60

        timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer)

        taskTitle = findViewById(R.id.taskTitle)
        timerText = findViewById(R.id.timerText)

        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)
        btnQuit = findViewById(R.id.btnQuit)

        tasks = JsonStorage.loadTasks(this)

        taskIndex = intent.getIntExtra("taskIndex", -1)

        val task = tasks[taskIndex]

        taskTitle.text = task.title

        totalTime = task.duration.toLong() * 60 * 1000
        timeLeft = totalTime

        updateTimer()

        btnStart.setOnClickListener { startTimer() }

        btnPause.setOnClickListener { pauseTimer() }

        btnReset.setOnClickListener {

            pauseTimer()
            timeLeft = totalTime
            updateTimer()

        }

        btnQuit.setOnClickListener {
            finish()
        }
    }
}