package com.example.exerciseactivitytracker.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.exerciseactivitytracker.databases.ExerciseDatabase
import com.example.exerciseactivitytracker.entities.Exercise
import com.example.exerciseactivitytracker.repositories.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExerciseRepository = ExerciseRepository(application)

    fun getAllExercisesManual(callback: (List<Exercise>) -> Unit) {
        viewModelScope.launch {
            val exercises = repository.getAllExercisesManual()
            callback(exercises)
        }
    }

    fun getExercisesByIdManual(id: Int, callback: (Exercise) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val exercise = repository.getExercisesByIdManual(id)
            withContext(Dispatchers.Main) {
                callback(exercise)
            }
        }
    }

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.insert(exercise)
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.update(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.delete(exercise)
        }
    }
}


