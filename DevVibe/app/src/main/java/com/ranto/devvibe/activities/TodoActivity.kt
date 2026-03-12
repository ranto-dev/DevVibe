package com.ranto.devvibe.activities

import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.adapters.TaskAdapter
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage


class PomodoroActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var adapter: TaskAdapter
    private var tasks = mutableListOf<Task>()

    override fun onResume() {
        super.onResume()
        tasks.clear()
        tasks.addAll(JsonStorage.loadTasks(this))
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ranto.devvibe.R.layout.activity_pomodoro)

        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)

        tasks = JsonStorage.loadTasks(this)

        adapter = TaskAdapter(this, tasks) { position ->
            val intent = Intent(this, AddEditTaskActivity::class.java)
            intent.putExtra("index", position)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }
    }
}