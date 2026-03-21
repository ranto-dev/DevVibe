package com.ranto.devvibe.activities

import android.content.*
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
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

    private lateinit var playlistLayout: LinearLayout
    private lateinit var playerLayout: LinearLayout

    private lateinit var btnStartPlaylist: Button
    private lateinit var btnMenu: ImageButton
    // MINI PLAYER
    private lateinit var miniPlayer: LinearLayout
    private lateinit var miniTitle: TextView
    private lateinit var miniPlayPause: Button
    private lateinit var miniOpenPlayer: Button

    private val handler = Handler(Looper.getMainLooper())

    private var musicService: MusicService? = null
    private var isBound = false

    private var currentTrackIndex = 0
    private var isLooping = false
    private var isUpdatingProgress = false

    private lateinit var statsManager: DevStatsManager

    private val tracks = listOf(
        Track("DJ MUSIC MIX 2026 VOL. 17 ⚡ Ultimate Mood Booster", R.raw.lofi1),
        Track("1 Hour Upbeat Background Music", R.raw.lofi2),
        Track("3 AM Coding Session - Lofi Hip Hop", R.raw.lofi3),
        Track("Cozy Guitar Lofi", R.raw.lofi4),
        Track("Fredji - Happy Life", R.raw.lofi5),
        Track("Ikson - Sunny", R.raw.lofi6),
        Track("Lofi Room Cafe Music", R.raw.lofi7),
        Track("Midnight Study Session", R.raw.lofi8),
        Track("Paradise (Official)", R.raw.lofi9),
    )

    private lateinit var adapter: TrackAdapter

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true

            musicService?.setTrackCompletionListener(this@MusicActivity)

            handler.post {
                syncUIWithPlayer()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onTrackCompleted() {
        runOnUiThread { playNextTrack() }
    }

    override fun onResume() {
        super.onResume()
        syncUIWithPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_music)

        statsManager = DevStatsManager(this)

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

        playlistLayout = findViewById(R.id.playlistLayout)
        playerLayout = findViewById(R.id.playerLayout)

        btnStartPlaylist = findViewById(R.id.btnStartPlaylist)
        btnMenu = findViewById(R.id.btnMenu)

        miniPlayer = findViewById(R.id.miniPlayer)
        miniTitle = findViewById(R.id.miniTitle)
        miniPlayPause = findViewById(R.id.miniPlayPause)
        miniOpenPlayer = findViewById(R.id.miniOpenPlayer)

        musicTitle.isSelected = true
        miniTitle.isSelected = true

        setupPlaylist()
        setupVolume()

        val intent = Intent(this, MusicService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        btnStartPlaylist.setOnClickListener {

            currentTrackIndex = 0
            loadTrack(tracks[0])

            playlistLayout.visibility = View.GONE
            playerLayout.visibility = View.VISIBLE
        }

        btnMenu.setOnClickListener {

            val popup = PopupMenu(this, btnMenu)

            popup.menu.add("\uD83D\uDCFB Voir la playlist")

            popup.setOnMenuItemClickListener {

                playerLayout.visibility = View.GONE
                playlistLayout.visibility = View.VISIBLE

                true
            }

            popup.show()
        }


        btnPlay.setOnClickListener {

            val service = musicService ?: return@setOnClickListener

            if (service.isPlaying()) {
                service.pauseMusic()
                btnPlay.text = "▶"
                miniPlayPause.text = "▶"
                vinylImage.clearAnimation()
            } else {
                service.resumeMusic()
                btnPlay.text = "⏸"
                miniPlayPause.text = "⏸"
                vinylImage.startAnimation(rotateAnimation)
            }
        }

        btnNext.setOnClickListener { playNextTrack() }

        btnPrev.setOnClickListener { playPreviousTrack() }

        btnLoop.setOnClickListener {

            isLooping = !isLooping
            musicService?.setLooping(isLooping)

            btnLoop.text = if (isLooping) "Loop On" else "Loop Off"
        }

        miniPlayPause.setOnClickListener {

            val service = musicService ?: return@setOnClickListener

            if (service.isPlaying()) {
                service.pauseMusic()
                btnPlay.text = "▶"
                miniPlayPause.text = "▶"
                vinylImage.clearAnimation()
            } else {
                service.resumeMusic()
                btnPlay.text = "⏸"
                miniPlayPause.text = "⏸"
                vinylImage.startAnimation(rotateAnimation)
            }
        }

        miniOpenPlayer.setOnClickListener {

            playlistLayout.visibility = View.GONE
            playerLayout.visibility = View.VISIBLE
        }

        musicProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {

                if (fromUser) musicService?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupPlaylist() {

        playlistRecycler.layoutManager = LinearLayoutManager(this)

        playlistRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        adapter = TrackAdapter(tracks) { track, index ->

            currentTrackIndex = index
            loadTrack(track)

            playlistLayout.visibility = View.GONE
            playerLayout.visibility = View.VISIBLE
        }

        playlistRecycler.adapter = adapter
    }

    private fun loadTrack(track: Track) {

        musicService?.playTrack(track.audioResId)

        musicTitle.text = track.title
        miniTitle.text = track.title

        btnPlay.text = "⏸"
        miniPlayPause.text = "⏸"

        vinylImage.startAnimation(rotateAnimation)

        miniPlayer.visibility = View.VISIBLE

        updateProgress()
    }

    private fun playNextTrack() {

        currentTrackIndex++

        if (currentTrackIndex >= tracks.size)
            currentTrackIndex = 0

        loadTrack(tracks[currentTrackIndex])
    }

    private fun playPreviousTrack() {

        currentTrackIndex--

        if (currentTrackIndex < 0)
            currentTrackIndex = tracks.size - 1

        loadTrack(tracks[currentTrackIndex])
    }

    private fun updateProgress() {

        if (isUpdatingProgress) return
        isUpdatingProgress = true

        handler.post(object : Runnable {

            override fun run() {

                val service = musicService ?: return

                val current = service.getCurrentPosition()
                val duration = service.getDuration()

                musicProgress.max = duration
                musicProgress.progress = current

                timeText.text =
                    "${formatTime(current)} / ${formatTime(duration)}"

                handler.postDelayed(this, 500)
            }
        })
    }

    private fun formatTime(ms: Int): String {

        val minutes = (ms / 1000) / 60
        val seconds = (ms / 1000) % 60

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun setupVolume() {

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        volumeSeek.max =
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        volumeSeek.progress =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeSeek.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

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
        })
    }

    private fun syncUIWithPlayer() {

        val service = musicService ?: return

        if (!service.hasTrack()) return

        val resId = service.currentTrackResId

        val index = tracks.indexOfFirst {
            it.audioResId == resId
        }

        if (index != -1) {

            currentTrackIndex = index

            val track = tracks[index]

            musicTitle.text = track.title
            miniTitle.text = track.title

            miniPlayer.visibility = View.VISIBLE
        }

        if (service.isPlaying()) {

            btnPlay.text = "⏸"
            miniPlayPause.text = "⏸"

            vinylImage.startAnimation(rotateAnimation)

        } else {

            btnPlay.text = "▶"
            miniPlayPause.text = "▶"

            vinylImage.clearAnimation()
        }

        updateProgress()
    }

    override fun onDestroy() {

        super.onDestroy()

        if (isBound) {

            unbindService(serviceConnection)
            isBound = false
        }

        handler.removeCallbacksAndMessages(null)
    }
}