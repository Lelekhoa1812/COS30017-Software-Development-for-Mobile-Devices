package com.example.rentwithintent

import android.content.Intent
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingTest {

    private lateinit var scenario: ActivityScenario<Booking>

    @Before
    fun setup() {
        val intent = Intent(getInstrumentation().targetContext, Booking::class.java)
        scenario = ActivityScenario.launch(intent)    // Launch intent
        scenario.moveToState(Lifecycle.State.RESUMED) // Ensure the activity is resumed
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testAllUIElementExist() {
        // Check if all elements exist at initial stage
        onView(withId(R.id.bookingDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.instrumentImage)).check(matches(isDisplayed()))
        onView(withId(R.id.setBorrowPeriodButton)).check(matches(isDisplayed()))
        onView(withId(R.id.confirmButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cancelButton)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()))
    }

    @Test
    fun testValidDatePicker() {
        onView(withId(R.id.setBorrowPeriodButton)).perform(click())
        // Set a date that represents a valid future date
        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2024, 12, 30)) // Use future date for valid date selection
        onView(withId(R.id.confirmTimeframeButton)).perform(click())
        // Confirm button shown state that date picking action is successful (else hidden underneath the DatePicker dialog)
        onView(withId(R.id.confirmButton)).check(matches(isDisplayed()))
        // Click the confirm button to finish the Booking activity
        onView(withId(R.id.confirmButton)).perform(click())
        // Launch MainActivity explicitly to ensure it's in the right state
        ActivityScenario.launch(MainActivity::class.java)
        // Check if instrumentPrice show, which state that current activity is Main after booking confirmation
        onView(withId(R.id.instrumentPrice)).check(matches(isDisplayed()))
    }

    @Test
    fun testInvalidDatePicker() {
        onView(withId(R.id.setBorrowPeriodButton)).perform(click())
        // Set a date that represents an invalid past date
        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2024, 9, 1)) // Use past date for invalid date selection
        onView(withId(R.id.confirmTimeframeButton)).perform(click())
        onView(withId(R.id.confirmButton)).check(doesNotExist()) // Expecting the button not to be displayed since we are still at the DatePicker dialog
    }

    @Test
    fun testCancelButtonCancelsBooking() {
        // Click the cancel button to finish the Booking activity
        onView(withId(R.id.cancelButton)).perform(click())
        // Launch MainActivity explicitly to ensure it's in the right state
        ActivityScenario.launch(MainActivity::class.java)
        // Check if instrumentPrice show, which state that current activity is Main after booking cancellation
        onView(withId(R.id.instrumentPrice)).check(matches(isDisplayed()))
    }

    // Function that returns a ViewAction to set a date on a DatePicker widget in Espresso tests
    private fun setDate(year: Int, month: Int, day: Int) = object : ViewAction {
        // The constraints ensure that the ViewAction only applies to DatePicker views
        override fun getConstraints(): Matcher<View> = isAssignableFrom(DatePicker::class.java)
        // Provides a description of what this ViewAction does, useful for logging and debugging
        override fun getDescription(): String = "Set the date on DatePicker"
        // uiController: Control the UI during the execution of this action
        // view: The target view on action, DatePicker
        override fun perform(uiController: UiController, view: View) {
            // received view = DatePicker
            val datePicker = view as DatePicker
            // Set the DatePicker's date (year, month, and day)
            // Month parameter is adjusted by minus 1 since DatePicker months are zero-based
            datePicker.updateDate(year, month - 1, day)
        }
    }
}
