package com.example.exerciseactivitytracker.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.exerciseactivitytracker.entities.Exercise

@Dao
interface ExerciseDAO {

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercisesManual(): List<Exercise>                 // Direct fetching method, no LiveData

    @Query("SELECT * FROM exercises WHERE activityTime = :date")
    fun getExercisesByDateManual(date: String): List<Exercise>  // Fetch by date without LiveData

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getExercisesByIdManual(id: Int): Exercise                // Fetch by id without LiveData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(exercise: Exercise)

    @Update
    fun update(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)
}
