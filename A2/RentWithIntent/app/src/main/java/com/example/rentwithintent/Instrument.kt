package com.example.rentwithintent

import android.os.Parcel
import android.os.Parcelable

// Data class representing a musical instrument with name, rating, price, category, and brand
data class Instrument(
    val name: String,
    var rating: Float,
    val price: Float,
    val category: String,
    val brand: String
) : Parcelable {
    // Constructor to read data from a Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",    // Read name string, if null, returns empty string
        parcel.readFloat(),                 // Read rating float
        parcel.readFloat(),                 // Read price float
        parcel.readString() ?: "", // Read category string, if null, returns empty string
        parcel.readString() ?: ""    // Read brand string, if null, returns empty string
    )

    // Write data to a Parcel for the Parcelable interface
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(rating)
        parcel.writeFloat(price)
        parcel.writeString(category)
        parcel.writeString(brand)
    }

    // A part of the Parcelable interface and is used to describe any special objects that are included in the Parcelable instance.
    // This indicate there is no special objects.
    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Instrument> {
        // Allow the instrument object to be de-parceled (converted back from its serialized state).
        // It ensures that the instrument's attributes are preserved across activities.
        override fun createFromParcel(parcel: Parcel): Instrument {
            return Instrument(parcel)
        }

        // Used when an array of Instrument objects needs to be created.
        // Not necessarily needed for this project's concept, yet still have to use to match the Parcelable framework.
        override fun newArray(size: Int): Array<Instrument?> {
            return arrayOfNulls(size)
        }
    }
}
