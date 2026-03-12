package com.ranto.devvibe.models

data class Task (
    var title: String,
    var duration: String,
    var isFinished: Boolean = false
)