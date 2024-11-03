package com.example.exerciseactivitytracker.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.exerciseactivitytracker.entities.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE activityTime LIKE :date")
    fun getExercisesByDate(date: String): LiveData<List<Exercise>>

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getExerciseById(id: Int): LiveData<Exercise>

}
