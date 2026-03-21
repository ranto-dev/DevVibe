package com.ranto.devvibe.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.models.TaskType
import com.ranto.devvibe.utils.JsonStorage
import com.ranto.devvibe.utils.TimeUtils
import com.ranto.devvibe.managers.DevStatsManager

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val onEdit: (Int) -> Unit,
    private val onOpenTimer: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleText)
        val durationText: TextView = view.findViewById(R.id.durationText)
        val timeBlockText: TextView = view.findViewById(R.id.timeBlockText)
        val typeText: TextView = view.findViewById(R.id.typeText)
        val checkFinished: CheckBox = view.findViewById(R.id.checkFinished)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    private fun updateTaskStyle(holder: TaskViewHolder, task: Task) {
        if (task.isFinished) {
            holder.titleText.paintFlags =
                holder.titleText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.titleText.setTextColor(Color.GRAY)
        } else {
            holder.titleText.paintFlags = 0
            holder.titleText.setTextColor(Color.BLACK)
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val task = tasks[position]
        val statsManager = DevStatsManager(context)

        // Texte
        holder.titleText.text = task.title

        // ⏱️ Time blocking
        holder.timeBlockText.text = "${task.startTime} - ${task.endTime}"

        val duration = TimeUtils.getDurationInMinutes(task.startTime, task.endTime)
        holder.durationText.text = TimeUtils.formatDuration(duration)

        // 🎯 Type
        holder.typeText.text = task.type.name

        when(task.type) {
            TaskType.DEEP_WORK -> holder.typeText.setTextColor(Color.RED)
            TaskType.LEARNING -> holder.typeText.setTextColor(Color.BLUE)
            TaskType.MEETING -> holder.typeText.setTextColor(Color.GREEN)
            TaskType.QUICK_TASK -> holder.typeText.setTextColor(Color.GRAY)
        }

        // Checkbox
        holder.checkFinished.setOnCheckedChangeListener(null)
        holder.checkFinished.isChecked = task.isFinished
        updateTaskStyle(holder, task)

        holder.checkFinished.setOnCheckedChangeListener { _, isChecked ->
            task.isFinished = isChecked
            JsonStorage.saveTasks(context, tasks)
            updateTaskStyle(holder, task)

            if (isChecked) {
                val updated = statsManager.updateDailyStreakOnce()
                if (updated) {
                    Toast.makeText(context, "🔥 Streak mis à jour !", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Edit
        holder.btnEdit.setOnClickListener {
            onEdit(position)
        }

        // Delete
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Supprimer")
                .setMessage("Confirmer la suppression ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    tasks.removeAt(position)
                    JsonStorage.saveTasks(context, tasks)
                    notifyDataSetChanged()
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler", null)
                .show()
        }

        // Click → Timer
        holder.itemView.setOnClickListener {
            if (!task.isFinished) {
                onOpenTimer(position)
            } else {
                Toast.makeText(context, "Déjà terminée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}