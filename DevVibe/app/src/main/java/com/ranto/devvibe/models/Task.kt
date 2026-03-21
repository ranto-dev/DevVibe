package com.ranto.devvibe.models

data class Task(
    var title: String,
    var description: String = "",
    var type: TaskType = TaskType.DEEP_WORK,
    var startTime: String = "",
    var endTime: String = "",
    var date: String = "",
    var priority: Int = 1,
    var isFinished: Boolean = false
)

enum class TaskType {
    DEEP_WORK,
    LEARNING,
    MEETING,
    QUICK_TASK
}