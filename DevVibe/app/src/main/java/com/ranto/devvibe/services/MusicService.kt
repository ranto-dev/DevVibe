package com.ranto.devvibe.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ranto.devvibe.R

class MusicService : Service() {

    interface TrackCompletionListener {
        fun onTrackCompleted()
    }

    private var completionListener: TrackCompletionListener? = null

    private val binder = MusicBinder()

    private var mediaPlayer: MediaPlayer? = null

    var currentTrackResId: Int = -1

    private val channelId = "devvibe_music_channel"
    private val notifId = 1

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun setTrackCompletionListener(listener: TrackCompletionListener) {
        completionListener = listener
    }

    fun playTrack(resId: Int) {

        try {

            currentTrackResId = resId

            mediaPlayer?.release()

            mediaPlayer = MediaPlayer.create(this, resId)

            mediaPlayer?.setOnCompletionListener {
                completionListener?.onTrackCompleted()
            }

            mediaPlayer?.start()

            createNotification("Playing music")

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun setLooping(loop: Boolean) {
        mediaPlayer?.isLooping = loop
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun hasTrack(): Boolean {
        return currentTrackResId != -1
    }

    @SuppressLint("ForegroundServiceType")
    private fun createNotification(title: String) {

        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "DevVibe Music",
                NotificationManager.IMPORTANCE_LOW
            )

            manager.createNotificationChannel(channel)
        }

        val notification: Notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("DevVibe Player")
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build()

        startForeground(notifId, notification)
    }

    override fun onDestroy() {

        mediaPlayer?.release()

        super.onDestroy()
    }
}