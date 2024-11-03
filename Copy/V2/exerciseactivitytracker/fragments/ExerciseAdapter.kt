package com.example.exerciseactivitytrackervariant.fragments

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.exerciseactivitytracker.R
import com.example.exerciseactivitytrackervariant.entities.Exercise

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private var onUpdateClick: (Exercise) -> Unit,
    private var onDeleteClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // ViewHolder elements inside the Exercise item
    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val activityTime: TextView = itemView.findViewById(R.id.activityTime)
        val activityName: TextView = itemView.findViewById(R.id.activityName)
        val duration: TextView = itemView.findViewById(R.id.duration)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    // Create View Holder with ExerciseViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_item, parent, false)
        return ExerciseViewHolder(view)
    }

    // Bind data and handle button trigger
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.activityTime.text = exercise.activityTime
        holder.activityName.text = exercise.activityName
        holder.duration.text = "${exercise.duration} hours" // Adapting format e.g., 2.5 hours
        // Handle Update button
        holder.updateButton.setOnClickListener { onUpdateClick(exercise) }
        // Handle Delete button
        holder.deleteButton.setOnClickListener {
            // Trigger the animation
            animateItemDeletion(holder.itemView) {
                onDeleteClick(exercise)  // Notify the deletion to the parent class
                notifyDataSetChanged()   // Refresh the adapter once deletion is confirmed
            }
        }
    }

    // Count the number of item
    override fun getItemCount() = exercises.size

    // Update entire list without mutating it
    fun updateExerciseList(newExercises: List<Exercise>) {
        this.exercises = newExercises
        notifyDataSetChanged()
    }

    // Get exercise listing
    fun getExercises(): List<Exercise> {
        return exercises
    }

    // Animation method to gradually change the background to red and fade out the item // UI Concurrency
    // Note: This animation method can simulate Visual Concurrency but not Actual-backend Concurrency as it does not process any actual background procedure
    private fun animateItemDeletion(view: View, onAnimationEnd: () -> Unit) {
        // Set default and destined color
        val colorFrom = ContextCompat.getColor(view.context, android.R.color.white)
        val colorTo = ContextCompat.getColor(view.context, android.R.color.holo_red_dark)

        // Method to gradually change color
        val colorAnimation = ObjectAnimator.ofObject(
            view, "backgroundColor", ArgbEvaluator(), colorFrom, colorTo
        )
        colorAnimation.duration = 1000 // 1 second to change color

        // Fading animation
        colorAnimation.addUpdateListener { animator ->
            if (animator.animatedFraction == 1f) {
                // After the color change, start the fade-out animation
                val fadeOut = AlphaAnimation(1f, 0f)
                fadeOut.duration = 1000 // 1 second to fade out
                fadeOut.fillAfter = true
                view.startAnimation(fadeOut)
                onAnimationEnd() // Notify parent after animation ends
            }
        }
        colorAnimation.start()
    }
}
