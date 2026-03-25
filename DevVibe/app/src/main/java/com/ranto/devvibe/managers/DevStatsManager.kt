package com.ranto.devvibe.managers

import android.content.Context
import androidx.core.content.edit

class DevStatsManager(context: Context) {
    private val prefs = context.getSharedPreferences("dev_stats", Context.MODE_PRIVATE)

    fun getCodingSessions(): Int {
        return prefs.getInt("coding_sessions", 0)
    }

    fun addFocusTime(durationMs: Long) {
        val current = prefs.getLong("focus_time", 0)
        prefs.edit { putLong("focus_time", current + durationMs) }
    }

    fun getFocusTimeMinutes(): Int {
        val ms = prefs.getLong("focus_time", 0)
        return (ms / (1000 * 60)).toInt()
    }

    fun updateDailyStreakOnce(): Boolean {
        val today = System.currentTimeMillis() / (1000 * 60 * 60 * 24) // jours depuis epoch
        val lastDay = prefs.getLong("last_streak_day", 0)
        var streak = prefs.getInt("daily_streak", 0)

        if (today == lastDay) return false
        streak = if (today - lastDay == 1L) streak + 1 else 1

        prefs.edit {
            putInt("daily_streak", streak)
            putLong("last_streak_day", today)
        }

        return true
    }

    fun updateDailyStreak() {
        val prefs = prefs
        val today = System.currentTimeMillis() / (1000*60*60*24)
        val lastDay = prefs.getLong("last_streak_day", 0)
        var streak = prefs.getInt("daily_streak", 0)

        if(today - lastDay == 1L) { // lendemain
            streak += 1
        } else if(today - lastDay > 1L) {
            streak = 1
        } else {
            return
        }

        prefs.edit {
            putInt("daily_streak", streak)
            putLong("last_streak_day", today)
        }
    }

    fun getDailyStreak(): Int {
        return prefs.getInt("daily_streak", 0)
    }
}