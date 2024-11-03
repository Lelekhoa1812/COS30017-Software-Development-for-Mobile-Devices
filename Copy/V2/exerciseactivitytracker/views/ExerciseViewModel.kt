package com.example.exerciseactivitytrackervariant.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exerciseactivitytrackervariant.databases.ExerciseDatabase
import com.example.exerciseactivitytrackervariant.entities.Exercise
import com.example.exerciseactivitytracker.repositories.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExerciseRepository = ExerciseRepository(application)

    // Replace the LiveData version with direct fetch methods for manual control
    fun getAllExercisesManual(): List<Exercise> {
        return repository.getAllExercisesManual() // This will directly query the database and return a list
    }

    fun getExercisesByDateManual(date: String): List<Exercise> {
        return repository.getExercisesByDateManual(date)
    }

    fun getExercisesByIdManual(id: Int): Exercise {
        return repository.getExercisesByIdManual(id)
    }

    fun insertExercise(exercise: Exercise) {
        repository.insert(exercise)
    }

    fun updateExercise(exercise: Exercise) {
        repository.update(exercise)
    }

    fun deleteExercise(exercise: Exercise) {
        repository.delete(exercise)
    }
}

