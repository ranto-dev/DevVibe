package com.ranto.devvibe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.databinding.ItemTaskBinding
import com.ranto.devvibe.models.Task

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (Int) -> Unit,
    private val onCheck: () -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val task = tasks[position]

        holder.binding.titleText.text = task.title
        holder.binding.durationText.text = "Duration: ${task.duration}"

        holder.binding.checkFinished.isChecked = task.isFinished

        holder.binding.checkFinished.setOnCheckedChangeListener { _, isChecked ->
            task.isFinished = isChecked
            onCheck()
        }

        holder.binding.btnDelete.setOnClickListener {
            onDelete(position)
        }

        holder.binding.btnEdit.setOnClickListener {
            onEdit(position)
        }
    }
}