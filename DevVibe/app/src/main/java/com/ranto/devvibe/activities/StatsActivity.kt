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

        // Stats existantes
        val sessions = statsManager.getCodingSessions()
        val focusHours = statsManager.getFocusHours()
        val bugs = statsManager.getBugsFixed()

        // Charger les tâches
        val tasks = JsonStorage.loadTasks(this)

        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.isFinished }
        val activeTasks = tasks.count { !it.isFinished }

        val completionProgress =
            if (totalTasks == 0) 0 else (completedTasks * 100) / totalTasks

        val stats = listOf(

            Stat(
                "Coding Sessions",
                "$sessions sessions",
                sessions % 100
            ),

            Stat(
                "Focus Time",
                "${focusHours}h",
                (focusHours % 100).toInt()
            ),

            Stat(
                "Bugs Fixed",
                "$bugs bugs"
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