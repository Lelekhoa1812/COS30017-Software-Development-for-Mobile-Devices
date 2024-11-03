package com.example.exerciseactivitytracker.repositories

import android.app.Application
import com.example.exerciseactivitytrackervariant.daos.ExerciseDAO
import com.example.exerciseactivitytrackervariant.databases.ExerciseDatabase
import com.example.exerciseactivitytrackervariant.entities.Exercise

class ExerciseRepository(private val application: Application) {

    private val exerciseDao: ExerciseDAO

    init {
        val db = ExerciseDatabase.getDatabase(application)
        exerciseDao = db.exerciseDao()
    }

    // Manual Data fetching methods
    fun getAllExercisesManual(): List<Exercise> {
        return exerciseDao.getAllExercisesManual() // Directly returns the data
    }

    fun getExercisesByDateManual(date: String): List<Exercise> {
        return exerciseDao.getExercisesByDateManual(date)
    }

    fun getExercisesByIdManual(id: Int): Exercise {
        return exerciseDao.getExercisesByIdManual(id)
    }

    fun insert(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    fun update(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }
}
