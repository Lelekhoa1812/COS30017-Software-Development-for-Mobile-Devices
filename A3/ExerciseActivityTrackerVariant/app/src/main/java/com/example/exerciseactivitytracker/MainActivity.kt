package com.example.exerciseactivitytracker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.exerciseactivitytracker.R

class MainActivity : AppCompatActivity() {
    // lateinit var sharedPreferences: SharedPreferences // Not used

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the toolbar as the ActionBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up navigation with the ActionBar to handle fragment title
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        // Ensure the fragment's title is displayed
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    // Inflate the menu to add the day/night icon
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu) // Inflate the menu where day/night icon will appear
        return true
    }

    // Handle click events on the day/night toggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.day_night_toggle -> {
                toggleTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Toggling between UI mode (theme) of light and dark mode
    private fun toggleTheme() {
        val currentNightMode =
            resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    // Handle navigation when the user presses the back button
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
