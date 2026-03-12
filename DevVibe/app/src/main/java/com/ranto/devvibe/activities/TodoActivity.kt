package com.ranto.devvibe.activities

import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
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
    private lateinit var emptyStateLayout: LinearLayout
    private var tasks = mutableListOf<Task>()
    private lateinit var btnRefresh: ImageButton

    override fun onResume() {

        super.onResume()

        tasks.clear()
        tasks.addAll(JsonStorage.loadTasks(this))

        adapter.notifyDataSetChanged()

        updateUI()
    }

    private fun updateUI() {

        if (tasks.isEmpty()) {

            emptyStateLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

        } else {

            emptyStateLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ranto.devvibe.R.layout.activity_pomodoro)

        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)
        btnRefresh = findViewById(R.id.btnRefresh)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        tasks = JsonStorage.loadTasks(this)

        adapter = TaskAdapter(
            this,
            tasks,
            onEdit = { position ->

                val intent = Intent(this, AddEditTaskActivity::class.java)
                intent.putExtra("index", position)
                startActivity(intent)

            },
            onOpenTimer = { position ->

                val intent = Intent(this, TimerActivity::class.java)
                intent.putExtra("taskIndex", position)
                startActivity(intent)

            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {

            startActivity(Intent(this, AddEditTaskActivity::class.java))

        }

        btnRefresh.setOnClickListener {

            tasks.clear()
            tasks.addAll(JsonStorage.loadTasks(this))

            adapter.notifyDataSetChanged()

            updateUI()

            Toast.makeText(this, "Liste mise à jour", Toast.LENGTH_SHORT).show()

        }

        updateUI()
    }
}