package com.example.exerciseactivitytracker.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exerciseactivitytracker.R
import com.example.exerciseactivitytracker.entities.Exercise
import com.example.exerciseactivitytracker.views.ExerciseViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class YourActivityFragment : Fragment() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var adapter: ExerciseAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_your_activity, container, false)
        // RecyclerView of exercise items
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // ViewModel
        exerciseViewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]
        // If there is a saved instance state, restore the exercises
        if (savedInstanceState != null) {
            val savedExercises = savedInstanceState.getParcelableArrayList("savedExercises", Bundle::class.java)?.map { bundle ->
                Exercise(
                    activityTime = bundle.getString("activityTime", ""),
                    activityName = bundle.getString("activityName", ""),
                    duration = bundle.getFloat("duration"),
                    id = bundle.getInt("id")
                )
            }
            // Re-Update the adapter with the restored exercises
            adapter = ExerciseAdapter(savedExercises ?: emptyList(), { exercise ->
                val action = YourActivityFragmentDirections
                    .actionYourActivityFragmentToUpdateActivityFragment(exercise.id)
                findNavController().navigate(action)
            }, { exercise ->
                exerciseViewModel.deleteExercise(exercise) // Delete with concurrency applied to delay 1s
                showUndoSnackbar(view, exercise)
            })
            recyclerView.adapter = adapter
        } else {
            // Get current date
            val currentDate = Calendar.getInstance().time
            Log.d("YourActivityFragment", "Extracted current datetime $currentDate") // Logs
            // Observe all exercises and filter out past ones, sort by date // LiveData
            exerciseViewModel.allExercises.observe(viewLifecycleOwner) { exercises ->
                val upcomingExercises = exercises.filter { exercise ->
                    val exerciseDateTime = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ) // get datetime in correct format from Locale
                        .parse(exercise.activityTime) // Parse both date and time from the activity
                    val currentDateTime = Calendar.getInstance().time // Current date and time
                    // Only include activities where the date is today but the time is in the future, or any future dates (exclude past event)
                    exerciseDateTime?.after(currentDateTime) ?: false ||
                            (exerciseDateTime != null && isSameDay(exerciseDateTime, currentDateTime) && !hasPassedToday(
                                exerciseDateTime,
                                currentDateTime
                            )) // External helper functions, scenario if exercise item is today but the time has passed
                }.sortedBy { exercise -> // Sorted
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .parse(exercise.activityTime)
//                    Log.d("YourActivityFragment - RecyclerView", "${exercise.activityTime} ${exercise.activityName}") // Logs // Order not refined
                }
                // Initialize the adapter with filtered and sorted data
                adapter = ExerciseAdapter(upcomingExercises, { exercise ->
                    // Navigate to Update Activity fragment, passing the corresponding exercise id
                    val action = YourActivityFragmentDirections
                        .actionYourActivityFragmentToUpdateActivityFragment(exercise.id)
                    Log.d(
                        "YourActivityFragment",
                        "Update activity button clicked with exercise activity name ${exercise.activityName}, id ${exercise.id}}"
                    ) // Logs
                    findNavController().navigate(action)
                }, { exercise ->
                    // Delete exercise activity
                    exerciseViewModel.deleteExercise(exercise) // Delete with concurrency applied to delay 1s
                    showUndoSnackbar(
                        view,
                        exercise
                    ) // Show SnackBar widget notify user upon deletion and allow restoration (undo)
                    Log.d("YourActivityFragment", "Delete activity button clicked with ${exercise.id}") // Logs
                })
                recyclerView.adapter = adapter
            }
        }

        // Listen to Filter by Date button
        view.findViewById<Button>(R.id.filterByDateButton).setOnClickListener {
            Log.d("YourActivityFragment", "Filter by Date button clicked") // Logs
            // Initialise DatePicker widget and context of day/month/year
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            // Format the DatePicker dialog with parsed data
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear) // month + 1 since they are 0-based
                    Log.d("YourActivityFragment - DatePicker", "Datetime selection $formattedDate") // Logs
                    // Filter the exercise list based on the selected date // LiveData
                    exerciseViewModel.getExercisesByDate(formattedDate).observe(viewLifecycleOwner) { exercises ->
                        adapter.updateExerciseList(exercises)
                    }
                },
                year, month, day
            )
            datePickerDialog.show() // Show DatePicker dialog
        }

        // Listen to Add Activity button
        view.findViewById<Button>(R.id.addActivityButton).setOnClickListener {
            // Navigate to Add Activity fragment
            findNavController().navigate(R.id.action_yourActivityFragment_to_addActivityFragment)
            Log.d("YourActivityFragment", "Add Activity button clicked") // Logs
        }

        return view
    }

    // Notify deletion action on a SnackBar and allow data restoration (UNDO) after deletion
    private fun showUndoSnackbar(view: View, deletedExercise: Exercise) {
        val snackbar = Snackbar.make(view, "Exercise activity removed", Snackbar.LENGTH_LONG)
        snackbar.setAction("UNDO") {
            // Re-insert the deleted exercise into the database when "UNDO" is clicked
            exerciseViewModel.insertExercise(deletedExercise)
            Log.d("YourActivityFragment - UNDO", "Exercise activity retrieved $deletedExercise") // Logs
        }
        snackbar.show()
    }

    // Function to check if two dates are the same day
    private fun isSameDay(date1: Date?, date2: Date?): Boolean {
        val calendar1 = Calendar.getInstance().apply { time = date1!! } // nullable
        val calendar2 = Calendar.getInstance().apply { time = date2!! } // nullable
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    // Function to check if the time of the exercise has already passed today
    private fun hasPassedToday(exerciseDateTime: Date?, currentDateTime: Date?): Boolean {
        return exerciseDateTime?.before(currentDateTime) ?: true
    }

    // create onSaveInstanceState to save filtered items not being reset upon orientation and theme change
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Store the filtered exercises into the Bundle
        val savedExerciseList = adapter.getExercises().map { exercise ->
            Bundle().apply {
                putString("activityTime", exercise.activityTime)
                putString("activityName", exercise.activityName)
                putFloat("duration", exercise.duration)
                putInt("id", exercise.id)
            }
        }
        // Use putParcelableArrayList to save the array list of exercise upon onSaveInstanceState
        outState.putParcelableArrayList("savedExercises", ArrayList(savedExerciseList))
    }
}
