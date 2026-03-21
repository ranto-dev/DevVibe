package com.ranto.devvibe.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage
import com.ranto.devvibe.utils.TimeUtils
import com.ranto.devvibe.managers.DevStatsManager

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
    private var running = false

    private var taskIndex = -1
    private var tasks = mutableListOf<Task>()

    private lateinit var statsManager: DevStatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

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

        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { confirmReset() }
        btnQuit.setOnClickListener { confirmQuit() }
    }

    private fun startTimer() {
        if (running) return

        countDownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                val task = tasks[taskIndex]
                task.isFinished = true
                JsonStorage.saveTasks(this@TimerActivity, tasks)

                statsManager.updateDailyStreak()
                statsManager.addFocusTime(totalTime)

                Toast.makeText(this@TimerActivity, "🎉 Tâche terminée !", Toast.LENGTH_LONG).show()
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

    private fun confirmReset() {
        AlertDialog.Builder(this)
            .setTitle("Réinitialiser")
            .setMessage("Voulez-vous vraiment réinitialiser le timer ?")
            .setPositiveButton("Oui") { _, _ ->
                pauseTimer()
                timeLeft = totalTime
                updateTimer()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun confirmQuit() {
        AlertDialog.Builder(this)
            .setTitle("Quitter")
            .setMessage("Voulez-vous vraiment quitter ?")
            .setPositiveButton("Quitter") { _, _ -> finish() }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun setMotivation(task: Task) {
        when(task.type.name) {
            "DEEP_WORK" -> {
                focusMessage.text = "🔥 Deep work en cours..."
                motivationText.text = "Le succès vient de la discipline."
            }
            "LEARNING" -> {
                focusMessage.text = "📚 Apprentissage en cours..."
                motivationText.text = "Chaque jour tu progresses."
            }
            "MEETING" -> {
                focusMessage.text = "🤝 Interaction en cours..."
                motivationText.text = "Communiquer c’est évoluer."
            }
            else -> {
                focusMessage.text = "⚡ Petite tâche rapide"
                motivationText.text = "Les petites actions comptent."
            }
        }
    }
}