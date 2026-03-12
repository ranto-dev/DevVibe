package com.ranto.devvibe.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Task
import com.ranto.devvibe.utils.JsonStorage

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val onEdit: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleText)
        val durationText: TextView = view.findViewById(R.id.durationText)
        val checkFinished: CheckBox = view.findViewById(R.id.checkFinished)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleText.text = task.title
        holder.durationText.text = task.duration
        holder.checkFinished.isChecked = task.isFinished

        // Checkbox toggle
        holder.checkFinished.setOnCheckedChangeListener { _, isChecked ->
            task.isFinished = isChecked
            JsonStorage.saveTasks(context, tasks)
            Toast.makeText(context, "Tâche marquée comme ${if (isChecked) "terminée" else "non terminée"}", Toast.LENGTH_SHORT).show()
        }

        // Edit action
        holder.btnEdit.setOnClickListener {
            onEdit(position)
        }

        // Delete action with confirmation
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Supprimer la tâche")
                .setMessage("Voulez-vous vraiment supprimer cette tâche ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    tasks.removeAt(position)
                    JsonStorage.saveTasks(context, tasks)
                    notifyDataSetChanged() // classique avant optimisation
                    Toast.makeText(context, "Tâche supprimée", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}