package com.ranto.devvibe.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage
import android.graphics.Color

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val onEdit: (Int) -> Unit,
    private val onOpenTimer: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleText)
        val durationText: TextView = view.findViewById(R.id.durationText)
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

        holder.titleText.text = task.title
        holder.durationText.text = task.duration

        holder.checkFinished.setOnCheckedChangeListener(null)
        holder.checkFinished.isChecked = task.isFinished

        updateTaskStyle(holder, task)

        holder.checkFinished.setOnCheckedChangeListener { _, isChecked ->
            task.isFinished = isChecked
            JsonStorage.saveTasks(context, tasks)

            updateTaskStyle(holder, task)

            Toast.makeText(
                context,
                "Tâche marquée comme ${if (isChecked) "terminée" else "non terminée"}",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.btnEdit.setOnClickListener {
            onEdit(position)
        }

        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Supprimer la tâche")
                .setMessage("Voulez-vous vraiment supprimer cette tâche ?")
                .setPositiveButton("Oui") { dialog, _ ->

                    tasks.removeAt(position)
                    JsonStorage.saveTasks(context, tasks)

                    notifyDataSetChanged()

                    Toast.makeText(context, "Tâche supprimée", Toast.LENGTH_SHORT).show()

                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // 👇 NOUVELLE FEATURE
        holder.itemView.setOnClickListener {

            if (!task.isFinished) {
                onOpenTimer(position)
            } else {
                Toast.makeText(context, "Cette tâche est déjà terminée", Toast.LENGTH_SHORT).show()
            }

        }
    }
}