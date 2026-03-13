package com.ranto.devvibe.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.adapters.StatAdapter
import com.ranto.devvibe.managers.DevStatsManager
import com.ranto.devvibe.models.Stat
import com.ranto.devvibe.utils.JsonStorage

class StatsActivity : AppCompatActivity() {

    private lateinit var statsRecycler: RecyclerView
    private lateinit var statsManager: DevStatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats)

        statsRecycler = findViewById(R.id.statsRecycler)
        statsManager = DevStatsManager(this)

        // Charger stats existantes
        val sessions = statsManager.getCodingSessions()
        val focusMinutes = statsManager.getFocusTimeMinutes()
        val hours = focusMinutes / 60
        val minutes = focusMinutes % 60

        val focusLabel = when {
            hours < 5 -> "Low Focus 😴"
            hours < 15 -> "Medium Focus 💪"
            else -> "High Focus 🔥"
        }

        // Charger les tâches
        val tasks = JsonStorage.loadTasks(this)
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.isFinished }
        val activeTasks = tasks.count { !it.isFinished }
        val completionProgress = if (totalTasks == 0) 0 else (completedTasks * 100) / totalTasks

        // Liste des stats
        val stats = listOf(
            Stat(
                "Coding Sessions",
                "$sessions sessions",
                sessions % 100
            ),
            Stat(
                "Focus Time",
                "$hours h $minutes min ($focusLabel)",
                (hours.coerceAtMost(20) * 5)
            ),
            Stat(
                "Daily Streak",
                "${statsManager.getDailyStreak()} jours",
                (statsManager.getDailyStreak().coerceAtMost(30) * 3)
            ),
            Stat(
                "Tâches en cours",
                "$activeTasks tâches"
            ),
            Stat(
                "Tâches terminées",
                "$completedTasks / $totalTasks",
                completionProgress
            )
        )

        statsRecycler.layoutManager = LinearLayoutManager(this)
        statsRecycler.adapter = StatAdapter(stats)
    }
}