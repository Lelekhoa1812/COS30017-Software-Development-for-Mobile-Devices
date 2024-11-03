package com.example.exerciseactivitytracker.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exerciseactivitytracker.databases.ExerciseDatabase
import com.example.exerciseactivitytracker.entities.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val exerciseDao = ExerciseDatabase.getDatabase(application).exerciseDao()
    val allExercises: LiveData<List<Exercise>> = exerciseDao.getAllExercises()

    // Use LiveData to list Exercise according to the date
    fun getExercisesByDate(date: String): LiveData<List<Exercise>> {
        return exerciseDao.getExercisesByDate("%$date%")
    }

    // Insert from DAO
    fun insertExercise(exercise: Exercise) = viewModelScope.launch {
        exerciseDao.insertExercise(exercise)
    }

    // Add concurrency with a delay before updating the exercise
    fun updateExercise(exercise: Exercise) = viewModelScope.launch(Dispatchers.IO) {
        delay(1000)  // Simulates 1-second delay before updating data
        exerciseDao.updateExercise(exercise)
    }

    // Get exercise item id by LiveData
    fun getExerciseById(id: Int): LiveData<Exercise> {
        return exerciseDao.getExerciseById(id)
    }

    // Add concurrency with a delay before deleting the exercise
    fun deleteExercise(exercise: Exercise) = viewModelScope.launch(Dispatchers.IO) {
        delay(1000)  // Simulates 1-second delay before deletion
        exerciseDao.deleteExercise(exercise)
    }
}
