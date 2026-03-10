package com.ranto.devvibe

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.adapters.StatAdapter
import com.ranto.devvibe.models.Stat

class StatsActivity : AppCompatActivity() {
    private lateinit var statsRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats)

        statsRecycler = findViewById(R.id.statsRecycler)

        val stats = listOf(
            Stat("Coding Sessions", "24 sessions", 80),
            Stat("Commits this week", "42 commits"),
            Stat("Focus time", "18h", 70),
            Stat("Bugs fixed", "13 bugs"),
            Stat("Projects active", "3 projects", 50)
        )

        statsRecycler.layoutManager = LinearLayoutManager(this)
        statsRecycler.adapter = StatAdapter(stats)
    }
}