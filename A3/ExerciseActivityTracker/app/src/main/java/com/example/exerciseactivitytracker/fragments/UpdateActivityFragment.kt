package com.example.exerciseactivitytracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.exerciseactivitytracker.R
import com.example.exerciseactivitytracker.entities.Exercise
import com.example.exerciseactivitytracker.views.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        val view = inflater.inflate(R.layout.fragment_update_activity, container, false) // inflates the layout fragment_update_activity.xml and initializes the UI components

        exerciseViewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]

        // Initialize the EditText fields, including Activity Name, Time Inputs and Duration Input
        val dayInput = view.findViewById<EditText>(R.id.dayInput)
        val monthInput = view.findViewById<EditText>(R.id.monthInput)
        val yearInput = view.findViewById<EditText>(R.id.yearInput)
        val hourInput = view.findViewById<EditText>(R.id.hourInput)
        val minuteInput = view.findViewById<EditText>(R.id.minuteInput)

        val activityNameInput = view.findViewById<EditText>(R.id.activityNameInput)
        charCountTextView = view.findViewById(R.id.wordCountTextView) // word count TextView (e.g., 0/20)

        val durationInput = view.findViewById<EditText>(R.id.durationInput)

        // Get the exercise using the passed id // Live Data
        exerciseViewModel.getExerciseById(args.exerciseId).observe(viewLifecycleOwner) { exercise ->
            currentExercise = exercise

            // Set the retrieved data to EditText fields
            // Split dateTime list into date and time component with an empty string, date components are split by a slash and time are split by semicolon
            val dateTime = currentExercise.activityTime.split(" ")
            val date = dateTime[0].split("/")
            val time = dateTime[1].split(":")

            // Get saved data from date and time of the exercise object with index
            dayInput.setText(date[0])
            monthInput.setText(date[1])
            yearInput.setText(date[2])
            hourInput.setText(time[0])
            minuteInput.setText(time[1])

            // Observe name and duration from exercise object
            activityNameInput.setText(currentExercise.activityName)
            durationInput.setText(currentExercise.duration.toString())
        }

        // Listen for activity name input changes and update character count
        activityNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val charCount = s?.length ?: 0
                charCountTextView.text = "$charCount/20" // Count name character TextView
                // Dynamically update character counting text reflecting color when exceeding the limit
                val colorRes = if (charCount > 20) R.color.red else R.color.green
                charCountTextView.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Handle submit button click
        view.findViewById<Button>(R.id.submitButton).setOnClickListener {
            // Validate and provide feedback using launch coroutines (propagate Toast feedbacks with delays)
            coroutineScope.launch {
                val errorMessages = mutableListOf<String>() //  Initialise mutable list of error Toast message

                // Validate Activity Time
                val day = dayInput.text.toString()
                val month = monthInput.text.toString()
                val year = yearInput.text.toString()
                val hour = hourInput.text.toString()
                val minute = minuteInput.text.toString()

                // Validate Datetime Format
                if (!isValidDateTime(day, month, year, hour, minute)) {
//                    Toast.makeText(requireContext(), "Invalid Time Format", Toast.LENGTH_SHORT).show() // Toast without concurrency
                    errorMessages.add("Invalid Time Format") // Toast with concurrency
                    Log.d("UpdateActivityFragment", "Invalid Activity Time") // Logs
                }

                // Validate Datetime Range
                if (!isValidRangeDateTime(day, month, hour, minute)) {
                    errorMessages.addAll(getRangeErrors(day, month, hour, minute))
                }

                // Validate Activity Name character count
                val activityName = activityNameInput.text.toString()
                val charCount = activityName.length
                if (charCount > 20 || charCount < 2) {
//                    Toast.makeText(requireContext(), "Invalid Activity Name", Toast.LENGTH_SHORT).show() // Toast without concurrency
                    errorMessages.add("Invalid Activity Name") // Toast with concurrency
                    Log.d("UpdateActivityFragment", "Invalid Activity Name") // Logs
                }

                // Validate Duration
                val durationText = durationInput.text.toString()
                val duration = durationText.toFloatOrNull()
                if (duration == null) {
//                    Toast.makeText(requireContext(), "Duration cannot be empty", Toast.LENGTH_SHORT).show() // Toast without concurrency
                    errorMessages.add("Duration cannot be empty") // Toast with concurrency
                    Log.d("UpdateActivityFragment", "Invalid Activity Duration") // Logs
                }

                // Display error Toast feedbacks sequentially (if not empty)
                if (errorMessages.isNotEmpty()) {
                    for (message in errorMessages) {
                        showToastSequentially(message)
                    }
                    return@launch // Not proceed through
                }

                // Update the current exercise item
                val activityTime = "$day/$month/$year $hour:$minute"
                currentExercise.apply {
                    this.activityTime = activityTime
                    this.activityName = activityName
                    if (duration != null) { // convert Float? to assert Float
                        this.duration = duration
                    }
                }
                exerciseViewModel.updateExercise(currentExercise) // Update with concurrency applied to delay 1s
                Toast.makeText(requireContext(), "Exercise Activity Updated!", Toast.LENGTH_SHORT).show() // Use Toast for customer feedback upon updating
                Log.d(
                    "UpdateActivityFragment",
                    "Exercise Activity Updated!$currentExercise"
                ) // Logs
                findNavController().navigate(R.id.action_updateActivityFragment_to_yourActivityFragment)
            }
        }

        // Handle cancel button click
        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            findNavController().navigate(R.id.action_updateActivityFragment_to_yourActivityFragment)
            Log.d("UpdateActivityFragment", "Cancel button clicked, navigate back") // Logs
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

    // Display Toast messages sequentially with delays // UI Concurrency
    private suspend fun showToastSequentially(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        delay(1000) // Delay for 1 seconds to allow each Toast to display before showing the next one
    }
}
