package com.ranto.devvibe.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.models.TaskType
import com.ranto.devvibe.utils.JsonStorage
import com.ranto.devvibe.utils.NotificationHelper

class AddEditTaskActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var editStartTime: EditText
    private lateinit var editEndTime: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var btnSave: Button
    private var tasks = mutableListOf<Task>()
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        editTitle = findViewById(R.id.editTitle)
        editDescription = findViewById(R.id.editDescription)
        editStartTime = findViewById(R.id.editStartTime)
        editEndTime = findViewById(R.id.editEndTime)
        spinnerType = findViewById(R.id.spinnerType)
        btnSave = findViewById(R.id.btnSave)

        tasks = JsonStorage.loadTasks(this)

        val types = TaskType.values().map { it.name }
        spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)

        index = intent.getIntExtra("index", -1)

        if (index != -1) {
            val task = tasks[index]
            editTitle.setText(task.title)
            editDescription.setText(task.description)
            editStartTime.setText(task.startTime)
            editEndTime.setText(task.endTime)
            spinnerType.setSelection(task.type.ordinal)
        }

        btnSave.setOnClickListener {
            val title = editTitle.text.toString()
            val description = editDescription.text.toString()
            val start = editStartTime.text.toString()
            val end = editEndTime.text.toString()
            val type = TaskType.values()[spinnerType.selectedItemPosition]

            if (title.isBlank() || start.isBlank() || end.isBlank()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (index == -1) {
                tasks.add(Task(title, description, type, start, end))
            } else {
                val task = tasks[index]
                task.title = title
                task.description = description
                task.type = type
                task.startTime = start
                task.endTime = end
            }

            JsonStorage.saveTasks(this, tasks)

            NotificationHelper.scheduleTaskNotification(
                this,
                title,
                tasks[if (index == -1) tasks.size - 1 else index].description,
                start
            )
            finish()
        }
    }
}