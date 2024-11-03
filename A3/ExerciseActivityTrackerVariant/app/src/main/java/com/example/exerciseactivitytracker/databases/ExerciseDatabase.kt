package com.example.exerciseactivitytracker.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.exerciseactivitytracker.daos.ExerciseDAO
import com.example.exerciseactivitytracker.entities.Exercise

@Database(entities = [Exercise::class], version = 1, exportSchema = true)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDAO

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
