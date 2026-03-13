package com.ranto.devvibe.activities

import android.annotation.SuppressLint
import android.content.*
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ranto.devvibe.R
import com.ranto.devvibe.adapters.TrackAdapter
import com.ranto.devvibe.managers.DevStatsManager
import com.ranto.devvibe.models.Track
import com.ranto.devvibe.services.MusicService

class MusicActivity : AppCompatActivity(),
    MusicService.TrackCompletionListener {

    private lateinit var vinylImage: ImageView
    private lateinit var rotateAnimation: Animation

    private lateinit var btnPlay: Button
    private lateinit var btnNext: Button
    private lateinit var btnPrev: Button
    private lateinit var btnLoop: Button

    private lateinit var musicTitle: TextView
    private lateinit var musicProgress: SeekBar
    private lateinit var timeText: TextView
    private lateinit var volumeSeek: SeekBar
    private lateinit var playlistRecycler: RecyclerView

    private lateinit var statsManager: DevStatsManager

    private val handler = Handler(Looper.getMainLooper())

    private var musicService: MusicService? = null
    private var isBound = false

    private var currentTrackIndex = 0
    private var isLooping = false

    private var sessionStartTime: Long = 0

    private val tracks = listOf(
        Track("Track 1", R.raw.lofi1),
        Track("Track 2", R.raw.lofi2),
        Track("Track 3", R.raw.lofi3),
        Track("Track 4", R.raw.lofi4),
        Track("Track 5", R.raw.lofi5),
        Track("Track 6", R.raw.lofi6),
        Track("Track 7", R.raw.lofi7),
        Track("Track 8", R.raw.lofi8),
        Track("Track 9", R.raw.lofi9),
    )

    private lateinit var adapter: TrackAdapter

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {

            val binder = service as MusicService.MusicBinder

            musicService = binder.getService()

            isBound = true

            musicService?.setTrackCompletionListener(this@MusicActivity)

            syncUIWithPlayer()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

            isBound = false
        }
    }

    override fun onTrackCompleted() {

        runOnUiThread {

            playNextTrack()

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupPlaylistRecycler() {

        playlistRecycler.layoutManager = LinearLayoutManager(this)

        playlistRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        adapter = TrackAdapter(tracks) { track, index ->

            currentTrackIndex = index

            adapter.currentTrackIndex = index

            adapter.notifyDataSetChanged()

            loadTrack(track)
        }

        adapter.currentTrackIndex = currentTrackIndex

        playlistRecycler.adapter = adapter
    }

    private fun loadTrack(track: Track) {

        musicService?.playTrack(track.audioResId)

        musicTitle.text = track.title

        btnPlay.text = "⏸"

        vinylImage.startAnimation(rotateAnimation)

        updateProgressBar()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun playNextTrack() {

        currentTrackIndex++

        if (currentTrackIndex >= tracks.size) {

            currentTrackIndex = 0
        }

        adapter.currentTrackIndex = currentTrackIndex

        adapter.notifyDataSetChanged()

        loadTrack(tracks[currentTrackIndex])
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun playPreviousTrack() {

        currentTrackIndex--

        if (currentTrackIndex < 0) {

            currentTrackIndex = tracks.size - 1
        }

        adapter.currentTrackIndex = currentTrackIndex

        adapter.notifyDataSetChanged()

        loadTrack(tracks[currentTrackIndex])
    }

    private fun updateProgressBar() {

        handler.post(object : Runnable {

            override fun run() {

                if (musicService?.isPlaying() == true) {

                    val current =
                        musicService?.getCurrentPosition() ?: 0

                    val duration =
                        musicService?.getDuration() ?: 0

                    musicProgress.max = duration

                    musicProgress.progress = current

                    timeText.text =
                        "${formatTime(current)} / ${formatTime(duration)}"
                }

                handler.postDelayed(this, 500)
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(ms: Int): String {

        val minutes = (ms / 1000) / 60

        val seconds = (ms / 1000) % 60

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun setupVolumeControl() {

        val audioManager =
            getSystemService(AUDIO_SERVICE) as AudioManager

        volumeSeek.max =
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        volumeSeek.progress =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeSeek.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        progress,
                        0
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_music)

        statsManager = DevStatsManager(this)

        vinylImage = findViewById(R.id.vinylImage)

        rotateAnimation =
            AnimationUtils.loadAnimation(this, R.anim.rotate)

        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnLoop = findViewById(R.id.btnLoop)

        musicTitle = findViewById(R.id.musicTitle)

        musicProgress = findViewById(R.id.musicProgress)

        timeText = findViewById(R.id.timeText)

        volumeSeek = findViewById(R.id.volumeSeek)

        playlistRecycler = findViewById(R.id.playlistRecycler)

        setupVolumeControl()

        setupPlaylistRecycler()

        val intent = Intent(this, MusicService::class.java)

        startService(intent)

        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        btnPlay.setOnClickListener {

            if (musicService?.isPlaying() == true) {

                musicService?.pauseMusic()

                btnPlay.text = "▶"

                vinylImage.clearAnimation()

                val endTime = System.currentTimeMillis()

                val duration = endTime - sessionStartTime

                statsManager.addFocusTime(duration)

            } else {

                musicService?.resumeMusic()

                btnPlay.text = "⏸"

                vinylImage.startAnimation(rotateAnimation)

                sessionStartTime = System.currentTimeMillis()

                statsManager.incrementCodingSession()
            }
        }

        btnNext.setOnClickListener {

            playNextTrack()
        }

        btnPrev.setOnClickListener {

            playPreviousTrack()
        }

        btnLoop.setOnClickListener {

            isLooping = !isLooping

            musicService?.setLooping(isLooping)

            btnLoop.text =
                if (isLooping) "Loop On" else "Loop Off"
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        if (isBound) {

            unbindService(serviceConnection)

            isBound = false
        }

        handler.removeCallbacksAndMessages(null)
    }

    private fun syncUIWithPlayer() {

        val service = musicService ?: return

        if (!service.hasTrack()) return

        val resId = service.currentTrackResId

        val index =
            tracks.indexOfFirst { it.audioResId == resId }

        if (index != -1) {

            currentTrackIndex = index

            adapter.currentTrackIndex = index

            adapter.notifyDataSetChanged()

            musicTitle.text = tracks[index].title
        }

        if (service.isPlaying()) {

            btnPlay.text = "⏸"

            vinylImage.startAnimation(rotateAnimation)

        } else {

            btnPlay.text = "▶"

            vinylImage.clearAnimation()
        }

        updateProgressBar()
    }
}