package com.ranto.devvibe.utils

import android.content.Context
import com.ranto.devvibe.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object JsonStorage {

    private const val FILE_NAME = "tasks.json"

    fun saveTasks(context: Context, tasks: List<Task>) {
        val json = Gson().toJson(tasks)
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(json)
    }

    fun loadTasks(context: Context): MutableList<Task> {

        val file = File(context.filesDir, FILE_NAME)

        if (!file.exists()) {
            return mutableListOf()
        }

        val json = file.readText()

        val type = object : TypeToken<MutableList<Task>>() {}.type

        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}