package com.ranto.devvibe.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage

class AddEditTaskActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var editDuration: EditText
    private lateinit var btnSave: Button

    private var tasks = mutableListOf<Task>()
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_task)

        editTitle = findViewById(R.id.editTitle)
        editDuration = findViewById(R.id.editDuration)
        btnSave = findViewById(R.id.btnSave)

        tasks = JsonStorage.loadTasks(this)

        index = intent.getIntExtra("index", -1)
        if (index != -1) {
            val task = tasks[index]
            editTitle.setText(task.title)
            editDuration.setText(task.duration)
        }

        btnSave.setOnClickListener {
            val title = editTitle.text.toString()
            val duration = editDuration.text.toString()

            if (title.isBlank() || duration.isBlank()) {
                return@setOnClickListener
            }

            if (index == -1) {
                tasks.add(Task(title, duration))
            } else {
                tasks[index].title = title
                tasks[index].duration = duration
            }

            JsonStorage.saveTasks(this, tasks)

            finish()
        }
    }
}