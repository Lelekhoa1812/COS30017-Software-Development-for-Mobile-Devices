package com.example.exerciseactivitytrackervariant.fragments

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
import com.example.exerciseactivitytrackervariant.entities.Exercise
import com.example.exerciseactivitytrackervariant.views.ExerciseViewModel
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        exerciseViewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]

        if (savedInstanceState != null) {
            val savedExercises = savedInstanceState.getParcelableArrayList("savedExercises", Bundle::class.java)?.map { bundle ->
                Exercise(
                    activityTime = bundle.getString("activityTime", ""),
                    activityName = bundle.getString("activityName", ""),
                    duration = bundle.getFloat("duration"),
                    id = bundle.getInt("id")
                )
            }
            // Re-update the adapter with the restored exercises
            adapter = ExerciseAdapter(savedExercises ?: emptyList(), { exercise ->
                val action = YourActivityFragmentDirections.actionYourActivityFragmentToUpdateActivityFragment(exercise.id)
                findNavController().navigate(action)
            }, { exercise ->
                exerciseViewModel.deleteExercise(exercise)
                showUndoSnackbar(view, exercise)
            })
            recyclerView.adapter = adapter
        } else {
            loadDataManually() // Load data manually if no saved instance
        }

        view.findViewById<Button>(R.id.filterByDateButton).setOnClickListener {
            showDatePickerDialog()
        }

        view.findViewById<Button>(R.id.addActivityButton).setOnClickListener {
            findNavController().navigate(R.id.action_yourActivityFragment_to_addActivityFragment)
        }

        return view
    }

    private fun loadDataManually() {
        val exercises = exerciseViewModel.getAllExercisesManual() // Fetch manually
        val filteredExercises = filterUpcomingExercises(exercises)
        adapter = ExerciseAdapter(filteredExercises, { exercise ->
            val action = YourActivityFragmentDirections.actionYourActivityFragmentToUpdateActivityFragment(exercise.id)
            findNavController().navigate(action)
        }, { exercise ->
            exerciseViewModel.deleteExercise(exercise)
            showUndoSnackbar(requireView(), exercise)
        })
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter = adapter
    }

    private fun filterUpcomingExercises(exercises: List<Exercise>): List<Exercise> {
        return exercises.filter { exercise ->
            val exerciseDateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(exercise.activityTime)
            val currentDateTime = Calendar.getInstance().time
            exerciseDateTime?.after(currentDateTime) ?: false ||
                    (exerciseDateTime != null && isSameDay(exerciseDateTime, currentDateTime) && !hasPassedToday(exerciseDateTime, currentDateTime))
        }.sortedBy {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(it.activityTime)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            val exercises = exerciseViewModel.getExercisesByDateManual(formattedDate)
            adapter.updateExerciseList(exercises)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun showUndoSnackbar(view: View, deletedExercise: Exercise) {
        val snackbar = Snackbar.make(view, "Exercise removed", Snackbar.LENGTH_LONG)
        snackbar.setAction("UNDO") {
            exerciseViewModel.insertExercise(deletedExercise)
            loadDataManually()
        }
        snackbar.show()
    }

    private fun isSameDay(date1: Date?, date2: Date?): Boolean {
        val calendar1 = Calendar.getInstance().apply { time = date1!! }
        val calendar2 = Calendar.getInstance().apply { time = date2!! }
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    private fun hasPassedToday(exerciseDateTime: Date?, currentDateTime: Date?): Boolean {
        return exerciseDateTime?.before(currentDateTime) ?: true
    }

    override fun onResume() {
        super.onResume()
        loadDataManually()
    }
}
