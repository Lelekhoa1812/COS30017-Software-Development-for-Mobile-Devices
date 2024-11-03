package com.example.exerciseactivitytracker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var activityTime: String,
    var duration: Float,
    var activityName: String
)
