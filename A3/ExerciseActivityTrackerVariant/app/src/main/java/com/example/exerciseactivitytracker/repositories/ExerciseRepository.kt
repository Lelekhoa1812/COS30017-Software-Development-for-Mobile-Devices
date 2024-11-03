package com.example.exerciseactivitytracker.repositories

import android.app.Application
import com.example.exerciseactivitytracker.daos.ExerciseDAO
import com.example.exerciseactivitytracker.databases.ExerciseDatabase
import com.example.exerciseactivitytracker.entities.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRepository(private val application: Application) {

    private val exerciseDao: ExerciseDAO

    init {
        val db = ExerciseDatabase.getDatabase(application)
        exerciseDao = db.exerciseDao()
    }

    suspend fun getAllExercisesManual(): List<Exercise> {
        return exerciseDao.getAllExercisesManual()
    }


    suspend fun getExercisesByIdManual(id: Int): Exercise {
        return withContext(Dispatchers.IO) {
            exerciseDao.getExercisesByIdManual(id)
        }
    }


    suspend fun insert(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            exerciseDao.insert(exercise)
        }
    }

    suspend fun update(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            exerciseDao.update(exercise)
        }
    }

    suspend fun delete(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            exerciseDao.delete(exercise)
        }
    }
}

