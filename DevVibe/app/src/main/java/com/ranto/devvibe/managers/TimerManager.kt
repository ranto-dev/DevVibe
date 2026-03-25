package com.ranto.devvibe.managers

import android.content.Context

object TimerManager {
    private const val PREF = "timer_pref"
    private const val KEY_END_TIME = "end_time"
    private const val KEY_RUNNING = "running"

    fun startTimer(context: Context, durationMillis: Long) {
        val endTime = System.currentTimeMillis() + durationMillis
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_END_TIME, endTime)
            .putBoolean(KEY_RUNNING, true)
            .apply()
    }

    fun stopTimer(context: Context) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_RUNNING, false).apply()
    }

    fun getTimeLeft(context: Context): Long {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val endTime = prefs.getLong(KEY_END_TIME, 0)
        return endTime - System.currentTimeMillis()
    }

    fun isRunning(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_RUNNING, false)
    }
}