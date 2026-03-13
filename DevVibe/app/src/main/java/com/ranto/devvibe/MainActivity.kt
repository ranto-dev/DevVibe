package com.ranto.devvibe
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ranto.devvibe.activities.AboutActivity
import com.ranto.devvibe.activities.MusicActivity
import com.ranto.devvibe.activities.PomodoroActivity
import com.ranto.devvibe.activities.QuoteActivity
import com.ranto.devvibe.activities.StatsActivity
import com.ranto.devvibe.activities.HelpActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnMenu = findViewById<MaterialButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {

            val popup = PopupMenu(this, btnMenu)

            popup.menu.add("Aide")
            popup.menu.add("A propos")

            popup.setOnMenuItemClickListener {

                when(it.title){

                    "Aide" -> {
                        startActivity(Intent(this, HelpActivity::class.java))
                        true
                    }

                    "A propos" -> {
                        startActivity(Intent(this, AboutActivity::class.java))
                        true
                    }

                    else -> false
                }

            }

            popup.show()
        }

        val cardMusic = findViewById<View>(R.id.cardMusic)
        val cardProject = findViewById<View>(R.id.cardProject)
        val cardStats = findViewById<View>(R.id.cardStats)
        val cardQuote = findViewById<View>(R.id.cardQuote)

        cardMusic.setOnClickListener {
            startActivity(Intent(this, MusicActivity::class.java))
        }

        cardProject.setOnClickListener {
            startActivity(Intent(this, PomodoroActivity::class.java))
        }

        cardStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        cardQuote.setOnClickListener {
            startActivity(Intent(this, QuoteActivity::class.java))
        }
    }
}