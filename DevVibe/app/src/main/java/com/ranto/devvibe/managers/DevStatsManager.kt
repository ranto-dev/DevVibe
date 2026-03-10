package com.ranto.devvibe.managers

import android.content.Context
import androidx.core.content.edit

class DevStatsManager(context: Context) {

    private val prefs = context.getSharedPreferences("dev_stats", Context.MODE_PRIVATE)

    fun incrementCodingSession() {
        val sessions = prefs.getInt("coding_sessions", 0)

        prefs.edit {
            putInt("coding_sessions", sessions + 1)
        }
    }

    fun getCodingSessions(): Int {
        return prefs.getInt("coding_sessions", 0)
    }

    fun addFocusTime(duration: Long) {
        val current = prefs.getLong("focus_time", 0)

        prefs.edit {
            putLong("focus_time", current + duration)
        }
    }

    fun getFocusHours(): Long {

        val ms = prefs.getLong("focus_time", 0)

        return ms / (1000 * 60 * 60)
    }

    fun incrementBugsFixed() {

        val bugs = prefs.getInt("bugs_fixed", 0)

        prefs.edit {
            putInt("bugs_fixed", bugs + 1)
        }
    }

    fun getBugsFixed(): Int {
        return prefs.getInt("bugs_fixed", 0)
    }

    fun setActiveProjects(count: Int) {

        prefs.edit {
            putInt("active_projects", count)
        }
    }

    fun getActiveProjects(): Int {
        return prefs.getInt("active_projects", 0)
    }
}