package com.ranto.devvibe

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.adapters.TrackAdapter
import com.ranto.devvibe.models.Track

class MusicActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vinylImage: ImageView
    private lateinit var rotateAnimation: android.view.animation.Animation

    private lateinit var btnPlay: Button
    private lateinit var btnNext: Button
    private lateinit var btnPrev: Button
    private lateinit var btnLoop: Button

    private lateinit var musicTitle: TextView
    private lateinit var musicProgress: SeekBar
    private lateinit var timeText: TextView
    private lateinit var volumeSeek: SeekBar
    private lateinit var playlistRecycler: RecyclerView

    private val handler = Handler(Looper.getMainLooper())

    private var currentTrackIndex = 0
    private var isLooping = false

    private val tracks = listOf(
        Track("Track 1",R.raw.lofi1),
        Track("Track 2",R.raw.lofi2),
        Track("Track 3",R.raw.lofi3),
        Track("Track 4",R.raw.lofi4),
        Track("Track 5",R.raw.lofi5),
        Track("Track 6",R.raw.lofi6),
        Track("Track 7",R.raw.lofi7),
        Track("Track 8",R.raw.lofi8),
        Track("Track 9",R.raw.lofi9),
    )

    private fun setupVolumeControl() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volumeSeek.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumeSeek.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun setupPlaylistRecycler() {
        playlistRecycler.layoutManager = LinearLayoutManager(this)
        playlistRecycler.adapter = TrackAdapter(tracks) { track, index ->
            currentTrackIndex = index
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(this, track.audioResId)
            musicTitle.text = track.title
            mediaPlayer.start()
            btnPlay.text = "⏸"
            vinylImage.startAnimation(rotateAnimation)
            updateProgressBar()
        }
    }

    private fun playNextTrack() {
        currentTrackIndex++
        if (currentTrackIndex >= tracks.size) currentTrackIndex = 0
        loadTrack(tracks[currentTrackIndex])
    }

    private fun playPreviousTrack() {
        currentTrackIndex--
        if (currentTrackIndex < 0) currentTrackIndex = tracks.size - 1
        loadTrack(tracks[currentTrackIndex])
    }

    private fun loadTrack(track: Track) {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, track.audioResId)
        musicTitle.text = track.title
        mediaPlayer.start()
        btnPlay.text = "⏸"
        vinylImage.startAnimation(rotateAnimation)
        updateProgressBar()
    }

    private fun updateProgressBar() {
        handler.post(object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    musicProgress.progress = mediaPlayer.currentPosition
                    musicProgress.max = mediaPlayer.duration
                    timeText.text = "${formatTime(mediaPlayer.currentPosition)} / ${formatTime(mediaPlayer.duration)}"
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun formatTime(ms: Int): String {
        val minutes = (ms / 1000) / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_music)

        vinylImage = findViewById(R.id.vinylImage)
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)

        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnLoop = findViewById(R.id.btnLoop)

        musicTitle = findViewById(R.id.musicTitle)
        musicProgress = findViewById(R.id.musicProgress)
        timeText = findViewById(R.id.timeText)
        volumeSeek = findViewById(R.id.volumeSeek)
        playlistRecycler = findViewById(R.id.playlistRecycler)

        mediaPlayer = MediaPlayer.create(this, tracks[currentTrackIndex].audioResId)
        musicTitle.text = tracks[currentTrackIndex].title

        setupVolumeControl()
        updateProgressBar()
        setupPlaylistRecycler()

        btnPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                btnPlay.text = "▶"
                vinylImage.clearAnimation()
            } else {
                mediaPlayer.start()
                btnPlay.text = "⏸"
                vinylImage.startAnimation(rotateAnimation)
            }
        }

        btnNext.setOnClickListener { playNextTrack() }
        btnPrev.setOnClickListener { playPreviousTrack() }

        btnLoop.setOnClickListener {
            isLooping = !isLooping
            mediaPlayer.isLooping = isLooping
            btnLoop.text = if (isLooping) "Loop On" else "Loop Off"
        }
    }
}