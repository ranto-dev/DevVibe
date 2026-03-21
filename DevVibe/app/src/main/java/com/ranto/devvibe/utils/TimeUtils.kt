package com.ranto.devvibe.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun getDurationInMinutes(start: String, end: String): Long {
        return try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val startDate = format.parse(start)
            val endDate = format.parse(end)

            val diff = endDate.time - startDate.time
            diff / (1000 * 60)
        } catch (e: Exception) {
            0
        }
    }

    fun formatDuration(minutes: Long): String {
        val h = minutes / 60
        val m = minutes % 60
        return if (h > 0) "${h}h ${m}min" else "${m}min"
    }
}