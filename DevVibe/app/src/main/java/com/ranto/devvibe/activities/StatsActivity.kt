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

class StatsActivity : AppCompatActivity() {
    private lateinit var statsRecycler: RecyclerView
    private lateinit var statsManager: DevStatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats)

        val recycler = findViewById<RecyclerView>(R.id.statsRecycler)
        statsManager = DevStatsManager(this)

        val sessions = statsManager.getCodingSessions()
        val focusHours = statsManager.getFocusHours()
        val bugs = statsManager.getBugsFixed()
        val projects = statsManager.getActiveProjects()

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
                "Active Projects",
                "$projects projects"
            )

        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = StatAdapter(stats)
    }
}