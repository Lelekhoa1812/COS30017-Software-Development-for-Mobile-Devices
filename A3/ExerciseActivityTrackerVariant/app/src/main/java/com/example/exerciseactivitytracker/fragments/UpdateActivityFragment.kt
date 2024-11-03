package com.example.exerciseactivitytracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.exerciseactivitytracker.R
import com.example.exerciseactivitytracker.entities.Exercise
import com.example.exerciseactivitytracker.views.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class UpdateActivityFragment : Fragment() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var currentExercise: Exercise
    private val args: UpdateActivityFragmentArgs by navArgs() // Use navArgs to parse exercise item's id, ensuring correct item is passed to this fragment

    private lateinit var charCountTextView: TextView

    private val coroutineScope = CoroutineScope(Dispatchers.Main) // Coroutine scope and Dispatchers for managing Toast feedbacks delays

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_activity, container, false)

        exerciseViewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]

        // Initialize the EditText fields
        val dayInput = view.findViewById<EditText>(R.id.dayInput)
        val monthInput = view.findViewById<EditText>(R.id.monthInput)
        val yearInput = view.findViewById<EditText>(R.id.yearInput)
        val hourInput = view.findViewById<EditText>(R.id.hourInput)
        val minuteInput = view.findViewById<EditText>(R.id.minuteInput)
        val activityNameInput = view.findViewById<EditText>(R.id.activityNameInput)
        val durationInput = view.findViewById<EditText>(R.id.durationInput)
        charCountTextView = view.findViewById(R.id.wordCountTextView)

        // Manually fetch the exercise item by its ID
//        exerciseViewModel.getExercisesByIdManual(args.exerciseId) { exercise ->
//            currentExercise = exercise
//        }
//        // Set retrieved exercise data to input fields
//        val dateTime = currentExercise.activityTime.split(" ")
//        val date = dateTime[0].split("/")
//        val time = dateTime[1].split(":")
//
//        dayInput.setText(date[0])
//        monthInput.setText(date[1])
//        yearInput.setText(date[2])
//        hourInput.setText(time[0])
//        minuteInput.setText(time[1])
//        activityNameInput.setText(currentExercise.activityName)
//        durationInput.setText(currentExercise.duration.toString())

        // Fetch the exercise item by its ID
        exerciseViewModel.getExercisesByIdManual(args.exerciseId) { exercise ->
            currentExercise = exercise

            // Set retrieved exercise data to input fields
            val dateTime = currentExercise.activityTime.split(" ")
            val date = dateTime[0].split("/")
            val time = dateTime[1].split(":")

            dayInput.setText(date[0])
            monthInput.setText(date[1])
            yearInput.setText(date[2])
            hourInput.setText(time[0])
            minuteInput.setText(time[1])
            activityNameInput.setText(currentExercise.activityName)
            durationInput.setText(currentExercise.duration.toString())
        }

        // Update Activity
        view.findViewById<Button>(R.id.submitButton).setOnClickListener {
            val day = dayInput.text.toString()
            val month = monthInput.text.toString()
            val year = yearInput.text.toString()
            val hour = hourInput.text.toString()
            val minute = minuteInput.text.toString()

            val activityTime = "$day/$month/$year $hour:$minute"
            val activityName = activityNameInput.text.toString()
            val duration = durationInput.text.toString().toFloatOrNull()

            // Update the exercise object with new data
            currentExercise.apply {
                this.activityTime = activityTime
                this.activityName = activityName
                if (duration != null) this.duration = duration
            }

            exerciseViewModel.updateExercise(currentExercise)
            Toast.makeText(requireContext(), "Exercise Activity Updated!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateActivityFragment_to_yourActivityFragment)
        }

        // Cancel update
        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            findNavController().navigate(R.id.action_updateActivityFragment_to_yourActivityFragment)
        }

        return view
    }


    // Method to validate date and time format (e.g, validate correct format dd/mm/yyyy HH:MM, entry not null)
    private fun isValidDateTime(day: String, month: String, year: String, hour: String, minute: String): Boolean {
        return day.length == 2 && month.length == 2 && year.length == 4 &&
                hour.length == 2 && minute.length == 2 &&
                day.toIntOrNull() != null && month.toIntOrNull() != null &&
                year.toIntOrNull() != null && hour.toIntOrNull() != null && minute.toIntOrNull() != null
    }

    // Collect and append error messages for invalid ranges to the list
    private fun getRangeErrors(day: String, month: String, hour: String, minute: String): List<String> {
        val errorMessages = mutableListOf<String>()
        val dayInt = day.toIntOrNull() ?: return listOf("Invalid Time: Day must be a number")
        val monthInt = month.toIntOrNull() ?: return listOf("Invalid Time: Month must be a number")
        val hourInt = hour.toIntOrNull() ?: return listOf("Invalid Time: Hour must be a number")
        val minuteInt = minute.toIntOrNull() ?: return listOf("Invalid Time: Minute must be a number")

        if (dayInt !in 1..31) errorMessages.add("Invalid Time: Day must be between 1 and 31")
        if (monthInt !in 1..12) errorMessages.add("Invalid Time: Month must be between 1 and 12")
        if (hourInt !in 0..23) errorMessages.add("Invalid Time: Hour must be between 0 and 23")
        if (minuteInt !in 0..59) errorMessages.add("Invalid Time: Minute must be between 0 and 59")

        return errorMessages
    }

    // Method to validate date and time input is within valid ranges, return Toast feedback reflecting issue
    private fun isValidRangeDateTime(day: String, month: String, hour: String, minute: String): Boolean {
        // Convert to int for numerical comparison
        val dayInt = day.toIntOrNull()
        val monthInt = month.toIntOrNull()
        val hourInt = hour.toIntOrNull()
        val minuteInt = minute.toIntOrNull()
        // Check valid ranges for date, month, hour, and minute
        if (dayInt !in 1..31) {
            Log.d("UpdateActivityFragment", "Invalid SetDate: $dayInt") // Logs
            return false
        }
        if (monthInt !in 1..12) {
            Log.d("UpdateActivityFragment", "Invalid SetMonth: $monthInt") // Logs
            return false
        }
        if (hourInt !in 0..23) {
            Log.d("UpdateActivityFragment", "Invalid SetHour: $hourInt") // Logs
            return false
        }
        if (minuteInt !in 0..59) {
            Log.d("UpdateActivityFragment", "Invalid SetMinute: $minuteInt") // Logs
            return false
        }
        // If all checks pass, return true
        return true
    }
}
