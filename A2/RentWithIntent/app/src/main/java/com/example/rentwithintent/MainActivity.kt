package com.example.rentwithintent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Listing instruments with name (string), rating (float), price (float), category (string), and brand (string)
    private val instruments = listOf(
        Instrument("Cordoba C5 Classical Guitar", 4.0f, 31.45f, "String", "Cordoba"),
        Instrument("Steinway Model B Grand Piano - Satin Ebony", 4.9f, 3429.75f, "Keyboard", "Steinway"),
        Instrument("Gliga Vasile Violin", 4.7f, 78.75f, "String", "Gliga Vasile"),
        Instrument("Yamaha Piaggero NP-15B 61Key Keyboard", 4.1f, 17.95f, "Keyboard", "Yamaha"),
        Instrument("Cambridge TR620L Trumpet", 3.9f, 49.75f, "Brass", "Cambridge"),
        Instrument("Yamaha YTS480 Intermediate Tenor Saxophone", 4.4f, 192.95f, "Brass", "Yamaha"),
        Instrument("Sanchez Soprano Ukulele - Natural Satin", 3.7f, 3.50f, "String", "Sanchez"),
        Instrument("Jet Black Pearl Roadshow Fusion Plus Drum Kit", 3.5f, 49.95f, "Drum", "Jet Black"),
        Instrument("Artist ST62 Fiesta Red Electric Guitar", 4.3f, 16.45f, "String", "Artist"),
        Instrument("Cambridge Double Key Tenor Flute", 4.2f, 24.80f, "Brass", "Cambridge"),
        Instrument("Yamaha SLB300 Silent Double Bass", 4.6f, 33.71f, "String", "Yamaha"),
        Instrument("Yamaha Stage Custom Birch Drum Kit", 4.4f, 82.45f, "Drum", "Yamaha")
    )

    // Set index to track current instrument, allow user switch to the next one
    private var currentInstrumentIndex = 0

    // Set filtered instruments equals to the instruments var at the beginning
    private var filteredInstruments = instruments

    // Define the ActivityResultLauncher
    private lateinit var bookingActivityLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Utilize savedInstanceState to remain the currentInstrumentIndex and filteredInstruments upon changing orientation
        // Restore saved state if exists
        if (savedInstanceState != null) {
            currentInstrumentIndex = savedInstanceState.getInt("currentInstrumentIndex", 0)
            filteredInstruments = savedInstanceState.getParcelableArrayList("filteredInstruments", Instrument::class.java) ?: instruments
        }
        // References to UI components: name, price, category, brand, rating bar, and image
        val nameTextView = findViewById<TextView>(R.id.instrumentName)
        val priceTextView = findViewById<TextView>(R.id.instrumentPrice)
        val categoryTextView = findViewById<TextView>(R.id.instrumentCategory)
        val brandTextView = findViewById<TextView>(R.id.instrumentBrand)
        val ratingBar = findViewById<RatingBar>(R.id.rateBar)
        val imageView = findViewById<ImageView>(R.id.instrumentImage)

        // References to UI button components
        val borrowButton = findViewById<Button>(R.id.borrowButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val filterButton = findViewById<Button>(R.id.filterButton)

        // Initialize the ActivityResultLauncher
        bookingActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) { // Booking activity passes the result confirming booking is successful.
                Toast.makeText(this, "Rental booking confirmed!", Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "Rental booking confirmed!")
                val updatedRating = result.data?.getFloatExtra("updatedRating", ratingBar.rating)
                // If user changed the rating at Booking page, alert changes
                if (updatedRating != filteredInstruments[currentInstrumentIndex].rating) {
                    filteredInstruments[currentInstrumentIndex].rating =
                        updatedRating ?: ratingBar.rating
                    displayInstrument(nameTextView, priceTextView, categoryTextView, brandTextView, ratingBar, imageView) // Update display upon changes
                    Toast.makeText(this, "Rating updated!", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Rating updated to $updatedRating")
                }
            }
        }

        // Check if instruments are available, hence adjust display and components' visibility
        displayInstrumentOrhandleNoResults(nameTextView, priceTextView, categoryTextView, brandTextView, ratingBar, imageView, nextButton, borrowButton)

        // "Next" button listener to display the next instrument in the list
        // Return to the first instrument when reaching maximum number of (filtered) instrument size
        nextButton.setOnClickListener {
            currentInstrumentIndex = (currentInstrumentIndex + 1) % filteredInstruments.size
            displayInstrument(nameTextView, priceTextView, categoryTextView, brandTextView, ratingBar, imageView)
            Log.d("MainActivity", "Next button clicked. Current instrument index: $currentInstrumentIndex")
        }

        // "Borrow" button listener to navigate to the booking page/screen with the current instrument
        borrowButton.setOnClickListener {
            val instrument = filteredInstruments[currentInstrumentIndex]
            Log.d("MainActivity", "Borrow button clicked for instrument: $instrument")
            // Pass the instrument details to the Booking activity using Intent
            val intent = Intent(this, Booking::class.java).apply {
                putExtra("instrument", filteredInstruments[currentInstrumentIndex])
            }
            bookingActivityLauncher.launch(intent) // Launch Booking activity
        }

        // "Filter" button listener to open filter modal dialog
        filterButton.setOnClickListener {
            Log.d("MainActivity", "Filter instrument button clicked.")
            val filterDialog = ChipFilterDialog.newInstance(instruments) { filtered ->
                filteredInstruments = filtered // Made change on the filteredInstruments var as now they only match the filtered element obtained from the chip widget
                currentInstrumentIndex = 0
                // Call display again upon filter
                displayInstrumentOrhandleNoResults(nameTextView, priceTextView, categoryTextView, brandTextView, ratingBar, imageView, nextButton, borrowButton)
            }
            // Show the dialog using supportFragmentManager to manage it as a support fragment
            filterDialog.show(supportFragmentManager, "ChipFilterDialog") // use supportFragmentManager to control the chip dialog as a support fragment
        }
    }

    // Using onSaveInstanceState to save data upon changing orientation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentInstrumentIndex", currentInstrumentIndex)
        outState.putParcelableArrayList("filteredInstruments", ArrayList<Parcelable>(filteredInstruments))
    }

    // Function to determine if instruments can be displayed or not, hence adjusting component's visibility
    private fun displayInstrumentOrhandleNoResults(
        nameTextView: TextView,
        priceTextView: TextView,
        categoryTextView: TextView,
        brandTextView: TextView,
        ratingBar: RatingBar,
        imageView: ImageView,
        nextButton: Button,
        borrowButton: Button
    ) {         // Case result, visibility set as default
        if (filteredInstruments.isNotEmpty()) {
            displayInstrument(nameTextView, priceTextView, categoryTextView, brandTextView, ratingBar, imageView)
            priceTextView.visibility = View.VISIBLE
            categoryTextView.visibility = View.VISIBLE
            brandTextView.visibility = View.VISIBLE
            ratingBar.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
            nextButton.visibility = View.VISIBLE
            borrowButton.visibility = View.VISIBLE
        } else { // Case no result, disable visibility
            handleNoResults(nameTextView)
            priceTextView.visibility = View.GONE
            categoryTextView.visibility = View.GONE
            brandTextView.visibility = View.GONE
            ratingBar.visibility = View.GONE
            imageView.visibility = View.GONE
            nextButton.visibility = View.GONE
            borrowButton.visibility = View.GONE
        }
    }
    
    // Function to display the current instrument's details (name, price, category, rating, and image)
    private fun displayInstrument(
        nameTextView: TextView,
        priceTextView: TextView,
        categoryTextView: TextView,
        brandTextView: TextView,
        ratingBar: RatingBar,
        imageView: ImageView
    ) {
        // Instrument to be displayed are the one filtered
        val instrument = filteredInstruments[currentInstrumentIndex]
        Log.d("MainActivity", "Displaying instrument: $instrument")

        // Set instrument details in the corresponding views
        nameTextView.text = instrument.name
        priceTextView.text = "Price: ${instrument.price} credits"
        categoryTextView.text = "Category: ${instrument.category}"
        brandTextView.text = "Brand: ${instrument.brand}"
        ratingBar.rating = instrument.rating

        // Set a corresponding image for each instrument
        when (instrument.name) {
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

    // Function handling scenario when no results are found
    private fun handleNoResults(
        nameTextView: TextView,
    ) {
        Toast.makeText(this, "Filter returns no matching result!", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", "Filter returns no matching result!")
        nameTextView.text = "No matching result, please try again."
    }
}
