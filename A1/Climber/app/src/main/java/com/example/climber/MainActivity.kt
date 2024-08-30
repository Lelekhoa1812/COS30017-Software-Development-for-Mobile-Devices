package com.example.climber

import android.content.res.Configuration
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private var score = 0
    private var currentHold = 0    // FLag to track current hold value
    private var hasFallen = false  // Flag to track if the climber has fallen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scoreText: TextView = findViewById(R.id.scoreText)
        val climbButton: Button = findViewById(R.id.climbButton)
        val fallButton: Button = findViewById(R.id.fallButton)
        val resetButton: Button = findViewById(R.id.resetButton)
        val languageSpinner: Spinner = findViewById(R.id.languageSpinner) // Spinner (drop-down) button to select languages

        // Set button colors
        climbButton.setBackgroundColor(ContextCompat.getColor(this, R.color.greenbutton))
        fallButton.setBackgroundColor(ContextCompat.getColor(this, R.color.redbutton))
        resetButton.setBackgroundColor(ContextCompat.getColor(this, R.color.bluebutton))

        // Restore saved state
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score", 0)
            currentHold = savedInstanceState.getInt("currentHold", 0)      // Restore current hold value
            hasFallen = savedInstanceState.getBoolean("hasFallen", false)  // Restore the fall state
            updateScoreDisplay(scoreText)
        }

        // Set up language dropdown
        setupLanguageSpinner(languageSpinner)

        // Listen to activity on climb button
        climbButton.setOnClickListener {
//            Log.d("ClimbButton", "Button clicked")
            if (!hasFallen && currentHold in 0..8) {  // Prevent climbing after falling and current hold must be in between 0-8
                currentHold++ // Increment current hold
                score += when (currentHold) {
                    in 1..3 -> 1
                    in 4..6 -> 2
                    in 7..9 -> 3
                    else -> 0
                }
                score = minOf(score, 18)  // Ensure the score does not exceed 18
                updateScoreDisplay(scoreText)
            }
            Log.d("ClimbingApp", "Climb button clicked, current hold: $currentHold, score: $score")
        }

        // Listen to activity on fall button
        fallButton.setOnClickListener {
//            Log.d("FallButton", "Button clicked")
            if (currentHold in 1..8) {
                score = maxOf(0, score - 3)
                currentHold = 0   // Reset current hold
                hasFallen = true  // Set the fall flag
                updateScoreDisplay(scoreText)
            }
            Log.d("ClimbingApp", "Fall button clicked, score: $score")
        }

        resetButton.setOnClickListener {
//            Log.d("ResetButton", "Button clicked")
            score = 0          // Reset
            currentHold = 0    // Reset
            hasFallen = false  // Reset
            updateScoreDisplay(scoreText)
            Log.d("ClimbingApp", "Reset button clicked, score reset to 0")
        }
    }

    private fun updateScoreDisplay(scoreText: TextView) {
        scoreText.text = score.toString()
        when (currentHold) {
            in 1..3 -> scoreText.setTextColor(getColor(R.color.blue))
            in 4..6 -> scoreText.setTextColor(getColor(R.color.green))
            in 7..9 -> scoreText.setTextColor(getColor(R.color.red))
        }
    }

    private fun setupLanguageSpinner(languageSpinner: Spinner) {
        // Set up language dropdown
        val languages = arrayOf("English", "Español", "Mandarin", "Vietnamese")                         // List of languages
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages) // adapt on changes made on the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Set language based on the current configuration
        val currentLanguage = Locale.getDefault().language
        languageSpinner.setSelection(
            when (currentLanguage) { // Switch cases by index
                "es" -> 1
                "zh" -> 2
                "vi" -> 3
                else -> 0
            }
        )

        // Handle language selection
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = when (position) {
                    1 -> "es"     // Spanish
                    2 -> "zh"     // Mandarin
                    3 -> "vi"     // Vietnamese
                    else -> "en"  // English
                }
                if (Locale.getDefault().language != selectedLanguageCode) {
                    changeLanguage(selectedLanguageCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    // Function to change language by replacing the language resource root folder
    private fun changeLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        // The updateConfiguration() method was deprecated in Android API level 25 (Nougat).
        // This method was deprecated because it manipulates the app's global resources, which can lead to inconsistencies.
        // However, this still works fine in this application as the function allow app's global configuration overriding the language config.
        // Which we only use 1 fragment (MainActivity) so the language switching function wouldn't trigger any misconfigurations.
        recreate()  // Restart the activity to apply changes
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)              // Save score
        outState.putInt("currentHold", currentHold)  // Save current hold
        outState.putBoolean("hasFallen", hasFallen)  // Save the fall state
    }
}
