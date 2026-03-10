package com.ranto.devvibe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Stat

class StatAdapter(private val stats: List<Stat>) :
    RecyclerView.Adapter<StatAdapter.StatViewHolder>() {

    class StatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statTitle: TextView = itemView.findViewById(R.id.statTitle)
        val statValue: TextView = itemView.findViewById(R.id.statValue)
        val statProgress: ProgressBar = itemView.findViewById(R.id.statProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stat, parent, false)

        return StatViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {

        val stat = stats[position]

        holder.statTitle.text = stat.title
        holder.statValue.text = stat.value

        if (stat.progress != null) {
            holder.statProgress.visibility = View.VISIBLE
            holder.statProgress.progress = stat.progress
        } else {
            holder.statProgress.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = stats.size
}