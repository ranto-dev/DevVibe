package com.ranto.devvibe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.models.Track

class TrackAdapter(
    private val tracks: List<Track>,
    private val onItemClick: (Track, Int) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        val track = tracks[position]

        holder.trackTitle.text = track.title

        holder.itemView.setOnClickListener {
            onItemClick(track, position)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}