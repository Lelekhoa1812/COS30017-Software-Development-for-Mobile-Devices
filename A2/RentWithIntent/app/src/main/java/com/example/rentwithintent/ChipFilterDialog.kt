package com.example.rentwithintent

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

// ChipFilterDialog extends DialogFragment to handle its lifecycle correctly.
class ChipFilterDialog : DialogFragment() {

    // Variables to hold instruments and the callback function for filtering results.
    private lateinit var instruments: List<Instrument>
    private lateinit var onFilterApplied: (List<Instrument>) -> Unit

    companion object {
        // newInstance method creates a new instance of ChipFilterDialog and sets up the necessary data
        fun newInstance(
            instruments: List<Instrument>, // List of instruments to display in the filter dialog
            onFilterApplied: (List<Instrument>) -> Unit // Callback function to apply filter results
        ): ChipFilterDialog {
            val fragment = ChipFilterDialog() // Create a new instance of the dialog fragment
            val args = Bundle().apply {
                // Save the instruments list in the arguments Bundle using Parcelable
                putParcelableArrayList("instruments", ArrayList(instruments))
            }
            fragment.arguments = args // Set the arguments to the fragment
            fragment.onFilterApplied = onFilterApplied // Set the callback function
            return fragment // Return the configured fragment instance
        }
    }

    // onCreateDialog method is used to create the dialog and set up its UI components.
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create the dialog with the parent context.
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.chip_dialog) // Set the custom layout for the dialog.

        // Retrieve instruments from the arguments Bundle passed via newInstance.
        instruments = arguments?.getParcelableArrayList("instruments", Instrument::class.java) ?: emptyList()

        // Get references to the chip groups and button within the dialog layout.
        val categoryChipGroup = dialog.findViewById<ChipGroup>(R.id.categoryChipGroup)
        val brandChipGroup = dialog.findViewById<ChipGroup>(R.id.brandChipGroup)
        val submitButton = dialog.findViewById<Button>(R.id.submitButton)

        // Extract distinct categories and brands from the list of instruments.
        val categories = instruments.map { it.category }.distinct()
        val brands = instruments.map { it.brand }.distinct()

        // Dynamically create chips for each category and add them to the category chip group.
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category // Set chip text to category name.
                isCheckable = true // Make the chip selectable.
            }
            categoryChipGroup.addView(chip) // Add the chip to the category chip group.
        }

        // Dynamically create chips for each brand and add them to the brand chip group.
        brands.forEach { brand ->
            val chip = Chip(requireContext()).apply {
                text = brand // Set chip text to brand name.
                isCheckable = true // Make the chip selectable.
            }
            brandChipGroup.addView(chip) // Add the chip to the brand chip group.
        }

        // Set an OnClickListener on the submit button to apply the selected filters.
        submitButton.setOnClickListener {
            // Retrieve the selected categories and brands from the checked chips.
            val selectedCategories = categoryChipGroup.checkedChipIds.map {
                dialog.findViewById<Chip>(it).text.toString()
            }
            val selectedBrands = brandChipGroup.checkedChipIds.map {
                dialog.findViewById<Chip>(it).text.toString()
            }

            Log.d("ChipFilterDialog", "Selected Categories: $selectedCategories, Selected Brands: $selectedBrands")

            // Filter the list of instruments based on the selected categories and brands
            val filtered = instruments.filter {
                (selectedCategories.isEmpty() || it.category in selectedCategories) &&
                        (selectedBrands.isEmpty() || it.brand in selectedBrands)
            }

            Log.d("ChipFilterDialog", "Filtered Instruments: $filtered")

            // Apply the filtered list using the callback function and dismiss the dialog
            onFilterApplied(filtered)
            dismiss()
        }

        return dialog // Return the constructed dialog
    }
}
