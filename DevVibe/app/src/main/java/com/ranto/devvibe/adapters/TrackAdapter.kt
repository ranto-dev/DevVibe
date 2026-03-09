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

    var currentTrackIndex: Int = -1

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

        // Highlight track actif
        holder.trackTitle.setBackgroundColor(
            if (position == currentTrackIndex) 0xFFE0E0E0.toInt() else 0x00000000
        )

        holder.itemView.setOnClickListener {
            onItemClick(track, position)
            // effet click rapide
            holder.itemView.alpha = 0.5f
            holder.itemView.animate().alpha(1f).setDuration(150).start()
        }
    }

    override fun getItemCount(): Int = tracks.size
}