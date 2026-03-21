package com.ranto.devvibe.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R
import com.ranto.devvibe.managers.DevStatsManager
import com.ranto.devvibe.managers.TimerManager
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage
import com.ranto.devvibe.utils.NotificationHelper
import com.ranto.devvibe.utils.TimeUtils

class TimerActivity : AppCompatActivity() {

    private lateinit var taskTitle: TextView
    private lateinit var taskDescription: TextView
    private lateinit var taskType: TextView
    private lateinit var timerText: TextView
    private lateinit var focusMessage: TextView
    private lateinit var motivationText: TextView

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button
    private lateinit var btnQuit: Button

    private lateinit var countDownTimer: CountDownTimer

    private var totalTime: Long = 0
    private var timeLeft: Long = 0

    private var taskIndex = -1
    private var tasks = mutableListOf<Task>()

    private lateinit var statsManager: DevStatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Bind UI
        taskTitle = findViewById(R.id.taskTitle)
        taskDescription = findViewById(R.id.taskDescription)
        taskType = findViewById(R.id.taskType)
        timerText = findViewById(R.id.timerText)
        focusMessage = findViewById(R.id.focusMessage)
        motivationText = findViewById(R.id.motivationText)

        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)
        btnQuit = findViewById(R.id.btnQuit)

        statsManager = DevStatsManager(this)

        // Load task
        tasks = JsonStorage.loadTasks(this)
        taskIndex = intent.getIntExtra("taskIndex", -1)
        val task = tasks[taskIndex]

        taskTitle.text = task.title
        taskDescription.text = task.description
        taskType.text = "Type : ${task.type}"

        setMotivation(task)

        val minutes = TimeUtils.getDurationInMinutes(task.startTime, task.endTime)
        totalTime = minutes * 60 * 1000
        timeLeft = totalTime

        updateTimer()

        // Buttons
        btnStart.setOnClickListener {
            TimerManager.startTimer(this, timeLeft)
            startLocalTimer()
        }

        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { confirmReset() }
        btnQuit.setOnClickListener { confirmQuit() }
    }

    override fun onResume() {
        super.onResume()
        if (TimerManager.isRunning(this)) {
            timeLeft = TimerManager.getTimeLeft(this)
            if (timeLeft <= 0) onTimerFinish() else startLocalTimer()
        }
    }

    private fun startLocalTimer() {
        countDownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = TimerManager.getTimeLeft(this@TimerActivity)
                updateTimer()
            }

            override fun onFinish() {
                onTimerFinish()
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        TimerManager.stopTimer(this)
    }

    private fun updateTimer() {
        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun confirmReset() {
        AlertDialog.Builder(this)
            .setTitle("Reset Timer")
            .setMessage("Are you sure you want to reset the timer?")
            .setPositiveButton("Yes") { _, _ ->
                pauseTimer()
                timeLeft = totalTime
                updateTimer()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmQuit() {
        AlertDialog.Builder(this)
            .setTitle("Quit")
            .setMessage("Are you sure you want to quit?")
            .setPositiveButton("Quit") { _, _ -> finish() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onTimerFinish() {
        TimerManager.stopTimer(this)
        val task = tasks[taskIndex]
        task.isFinished = true
        JsonStorage.saveTasks(this, tasks)
        statsManager.updateDailyStreak()
        statsManager.addFocusTime(totalTime)
        NotificationHelper.showNotification(this)
        Toast.makeText(this, "🎉 Task finished !", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun setMotivation(task: Task) {
        when (task.type.name) {
            "DEEP_WORK" -> {
                focusMessage.text = "🔥 Deep work in progress..."
                motivationText.text = "Success comes from discipline."
            }
            "LEARNING" -> {
                focusMessage.text = "📚 Learning in progress..."
                motivationText.text = "Every day you get better."
            }
            "MEETING" -> {
                focusMessage.text = "🤝 Meeting in progress..."
                motivationText.text = "Communication is growth."
            }
            else -> {
                focusMessage.text = "⚡ Quick task underway"
                motivationText.text = "Small actions make a difference."
            }
        }
    }
}