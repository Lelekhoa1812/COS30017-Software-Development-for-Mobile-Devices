package com.example.rentwithintent

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(Intent(getInstrumentation().targetContext, MainActivity::class.java))
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testAllUIElementExist() {
        // Check if all elements exist at initial stage
        onView(withId(R.id.instrumentName)).check(matches(isDisplayed()))
        onView(withId(R.id.instrumentPrice)).check(matches(isDisplayed()))
        onView(withId(R.id.instrumentCategory)).check(matches(isDisplayed()))
        onView(withId(R.id.instrumentBrand)).check(matches(isDisplayed()))
        onView(withId(R.id.rateBar)).check(matches(isDisplayed()))
        onView(withId(R.id.instrumentImage)).check(matches(isDisplayed()))
        onView(withId(R.id.borrowButton)).check(matches(isDisplayed()))
        onView(withId(R.id.nextButton)).check(matches(isDisplayed()))
        onView(withId(R.id.filterButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testNextButtonNavigatesToNextInstrument() {
        // Check if initial instrument is displayed
        onView(withId(R.id.instrumentName)).check(matches(withText("Cordoba C5 Classical Guitar")))
        // Click the next button
        onView(withId(R.id.nextButton)).perform(click())
        // Check if the next instrument is displayed (no longer show the first instrument)
        onView(withId(R.id.instrumentName)).check(matches(not(withText("Cordoba C5 Classical Guitar")))).check(matches(withText("Steinway Model B Grand Piano - Satin Ebony")))
    }

    @Test
    fun testBorrowButtonOpensBookingActivity() {
        // Click the borrow button
        onView(withId(R.id.borrowButton)).perform(click())
        // Check if Booking Activity is opened, bookingDetails UI component is one of the unique UI element in Booking activity
        onView(withId(R.id.bookingDetails)).check(matches(isDisplayed()))
    }

    @Test
    fun testFilterButtonOpensFilterDialog() {
        // Click the filter button
        onView(withId(R.id.filterButton)).perform(click())
        // Check if the filter dialog is displayed, submitButton is one of the unique element of the chip filter dialog
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testNoResultsDisplayWhenFilterReturnsEmpty() {
        // Open filter dialog and apply a filter that returns no results
        onView(withId(R.id.filterButton)).perform(click())
        // Apply chips with no matching result (e.g., Steinway and String don't match)
        onView(withText("Steinway")).perform(click())
        onView(withText("String")).perform(click())
        // Submit filter
        onView(withId(R.id.submitButton)).perform(click())
        // Check if the 'No matching result' message is displayed
        onView(withId(R.id.instrumentName)).check(matches(withText("No matching result, please try again.")))
        // Check hidden elements upon no matching result
        onView(withId(R.id.instrumentPrice)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.instrumentCategory)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.instrumentBrand)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.rateBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.instrumentImage)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.borrowButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.nextButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testValidResultsDisplayWhenFilterReturnsEmpty() {
        // Open filter dialog and apply a filter that returns valid results
        onView(withId(R.id.filterButton)).perform(click())
        // Apply chip with 1 valid matching result (e.g. Steinway is categorised as "Keyboard" so it's true)
        onView(withText("Steinway")).perform(click())
        onView(withText("Keyboard")).perform(click())
        // Submit filter
        onView(withId(R.id.submitButton)).perform(click())
        // Check if the the correct instrument message is displayed
        onView(withId(R.id.instrumentName)).check(matches(withText("Steinway Model B Grand Piano - Satin Ebony")))
    }
}