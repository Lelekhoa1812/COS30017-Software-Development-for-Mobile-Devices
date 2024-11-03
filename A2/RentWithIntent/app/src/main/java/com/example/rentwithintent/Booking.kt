package com.example.rentwithintent

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.concurrent.TimeUnit

class Booking : AppCompatActivity() {
    // Set start and end date var to allow user manage their booking period
    private var endDate: Long? = null
    // Tracking boolean var as if user has set the valid borrowing period
    private var validDate: Boolean = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Retrieve the passed instrument object from the Intent
        //val instrument = intent.getParcelableExtra<Instrument>("instrument")
        // The above getParcelableExtra function has been deprecated as it uses a generic type (T) which could cause runtime error
        // Newer version was updated with class type specified.
        val instrument = intent.getParcelableExtra("instrument", Instrument::class.java)

        Log.d("BookingActivity", "Received instrument: $instrument")

        // References to UI components in the booking screen (details and buttons)
        val bookingDetailsTextView = findViewById<TextView>(R.id.bookingDetails)
        val imageView = findViewById<ImageView>(R.id.instrumentImage)
        val setBorrowPeriodButton = findViewById<Button>(R.id.setBorrowPeriodButton)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        // Display the instrument details and image in the booking screen
        instrument?.let {
            setImage(it.name, imageView)
            bookingDetailsTextView.text = "Booking for ${it.name}.\nPrice per month: ${it.price} credits \nNotice that rental prices are applied on monthly periodic term."
            ratingBar.rating = it.rating
        }

        // Set Borrow Period button listener to manage booking timeframe, triggering the DatePicker dialog
        setBorrowPeriodButton.setOnClickListener {
            showDatePickerDialog()
            Log.d("BookingActivity", "Borrow Period button clicked.")
        }

        // Confirm button listener to finalize the booking
        confirmButton.setOnClickListener {
            if (validDate == false) {
                Toast.makeText(this, "Please pick your borrowing period!", Toast.LENGTH_SHORT).show()
                Log.d("BookingActivity", "Borrow period not defined.")
            }
            else {
                val currentTime = Calendar.getInstance().timeInMillis // Get the current time data using timeInMillis
                val borrowPeriod = TimeUnit.MILLISECONDS.toDays(endDate!! - currentTime).toInt()
                Toast.makeText(this, "Booking Confirmed! You are borrowing ${instrument?.name} for $borrowPeriod days.", Toast.LENGTH_SHORT).show()
                Log.d("BookingActivity", "Booking confirmed for ${instrument?.name} for $borrowPeriod days.")
                val intent = Intent().apply { // Pass intent object back to Main activity page upon changes on booking status and rating
                    putExtra("isBooked", true)
                    putExtra("updatedRating", ratingBar.rating)
                }
                setResult(RESULT_OK, intent)
                finish() // Finish the activity and return to the previous (main) page/screen
            }
        }

        // Cancel button listener to cancel the booking
        cancelButton.setOnClickListener {
            Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show()
            Log.d("BookingActivity", "Booking cancelled for ${instrument?.name}")
            setResult(RESULT_CANCELED) // Cancel booking
            finish() // Finish the activity and return to the previous (main) page/screen
        }
    }

    // Set the image based on the instrument name
    private fun setImage(instrumentName: String, imageView: ImageView) {
        when (instrumentName) {
            "Cordoba C5 Classical Guitar" -> imageView.setImageResource(R.drawable.guitar)
            "Steinway Model B Grand Piano - Satin Ebony" -> imageView.setImageResource(R.drawable.piano)
            "Gliga Vasile Violin" -> imageView.setImageResource(R.drawable.violin)
            "Yamaha Piaggero NP-15B 61Key Keyboard" -> imageView.setImageResource(R.drawable.keyboard)
            "Cambridge TR620L Trumpet" -> imageView.setImageResource(R.drawable.trumpet)
            "Yamaha YTS480 Intermediate Tenor Saxophone" -> imageView.setImageResource(R.drawable.saxophone)
            "Sanchez Soprano Ukulele - Natural Satin" -> imageView.setImageResource(R.drawable.ukulele)
            "Jet Black Pearl Roadshow Fusion Plus Drum Kit" -> imageView.setImageResource(R.drawable.drum)
            "Artist ST62 Fiesta Red Electric Guitar" -> imageView.setImageResource(R.drawable.eguitar)
            "Cambridge Double Key Tenor Flute" -> imageView.setImageResource(R.drawable.flute)
            "Yamaha SLB300 Silent Double Bass" -> imageView.setImageResource(R.drawable.doublebass)
            "Yamaha Stage Custom Birch Drum Kit" -> imageView.setImageResource(R.drawable.drumkit)
        }
    }

    // Construct and display DatePicker dialog
    private fun showDatePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.datepicker_dialog) // set the dialog layout
        // Adjust DatePicker dialog sizing to allow better UI/UX
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(), // Width wrap 80% of their parent width
            ViewGroup.LayoutParams.WRAP_CONTENT                   // Height remains wrap content
        )
        dialog.show()
        // Find views for the DatePicker calendars and confirm button
        val endDatePicker = dialog.findViewById<DatePicker>(R.id.endDatePicker)
        val confirmTimeframeButton = dialog.findViewById<Button>(R.id.confirmTimeframeButton)

        // Confirm button listener to finalize the booking
        // Update on that the app allows for immediate pickup only, so start day is not required any more
        confirmTimeframeButton.setOnClickListener {
            val endCalendar = Calendar.getInstance()
            endCalendar.set(endDatePicker.year, endDatePicker.month, endDatePicker.dayOfMonth)
            val selectedEndDate = endCalendar.timeInMillis
            val currentTime = Calendar.getInstance().timeInMillis // Get the current time data using timeInMillis
            // Time validation logic
            when {
                selectedEndDate <= currentTime -> {   // Selected timeframe must have the end day after the current date
                    Toast.makeText(this, "The end date must be after today!", Toast.LENGTH_SHORT).show()
                    Log.d("BookingActivity - DatePickerDialog", "End date must be after today.") // Log
                }
                else -> {
                    // Save the appropriate/valid dates of rental ending
                    endDate = selectedEndDate
                    Toast.makeText(this, "Borrow period set!", Toast.LENGTH_SHORT).show()
                    Log.d("BookingActivity - DatePickerDialog", "Set end date $selectedEndDate") // Log
                    dialog.dismiss()
                    validDate = true // Change valid date tracker to true
                }
            }
        }
        dialog.show()
    }
}
