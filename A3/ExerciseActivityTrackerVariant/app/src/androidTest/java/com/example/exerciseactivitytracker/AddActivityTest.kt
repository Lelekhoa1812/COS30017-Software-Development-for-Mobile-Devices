package com.example.exerciseactivitytracker


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    // Test UI elements exist
    @Test
    fun UIElementExist() {
        // Click button navigates from Your Activity to Add Activity fragment
        val addButton = onView(
            allOf(
                withId(R.id.addActivityButton), withText("Add Activity"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 1),
                isDisplayed()))
        addButton.perform(click())
        // Wait for the fragment to load
        Thread.sleep(1000)
        // Check customized ToolBar widget showing correct fragment (page) name - Add Activity
        val toolBar = onView(
            allOf(
                withText("Add Activity"),
                withParent(
                    allOf(
                        withId(R.id.toolbar),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
                isDisplayed()))
        toolBar.check(matches(withText("Add Activity")))
        // Check UI elements in Add Activity Fragment exist
        // Activity Time text
        val actTimeText = onView(
            allOf(
                withId(R.id.activityTimeText), withText("Activity Time"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        actTimeText.check(matches(withText("Activity Time")))
        // Check Activity Date and Time inputs (with hint)
        onView(withId(R.id.dayInput)).check(matches(isDisplayed()))
        onView(withId(R.id.monthInput)).check(matches(isDisplayed()))
        onView(withId(R.id.yearInput)).check(matches(isDisplayed()))
        onView(withId(R.id.hourInput)).check(matches(isDisplayed()))
        onView(withId(R.id.minuteInput)).check(matches(isDisplayed()))
        // Activity Name text
        val actNameText = onView(
            allOf(
                withId(R.id.activityNameText), withText("Activity Name"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        actNameText.check(matches(withText("Activity Name")))
        // Activity Name input (with hint)
        onView(withId(R.id.activityNameInput)).check(matches(isDisplayed()))
        // Character counter (initially 0/20)
        val charCount = onView(
            allOf(
                withId(R.id.wordCountTextView), withText("0/20"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        charCount.check(matches(isDisplayed()))
        // Duration text
        val durText = onView(
            allOf(
                withId(R.id.durationText), withText("Duration"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        durText.check(matches(isDisplayed()))
        // Duration input
        onView(withId(R.id.durationInput)).check(matches(isDisplayed()))
        // Submit button
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()))
        // Cancel button
        onView(withId(R.id.cancelButton)).check(matches(isDisplayed()))
    }

    // Check if adding exercise item success
    @Test
    fun AddExerciseActivity(){
        // Click button navigates from Your Activity to Add Activity fragment
        val addButton = onView(
            allOf(
                withId(R.id.addActivityButton), withText("Add Activity"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 1),
                isDisplayed()))
        addButton.perform(click())
        // Wait for the fragment to load
        Thread.sleep(1000)
        // Fill in datetime data for the exercise item
        // Day as 18
        val dayInput = onView(
            allOf(
                withId(R.id.dayInput),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 0),
                isDisplayed()))
        dayInput.perform(replaceText("18"), closeSoftKeyboard())
        // Month as 12
        val monthInput = onView(
            allOf(
                withId(R.id.monthInput),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 2),
                isDisplayed()))
        monthInput.perform(replaceText("12"), closeSoftKeyboard())
        // Year as 2003
        val yearInput = onView(
            allOf(
                withId(R.id.yearInput),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 4),
                isDisplayed()))
        yearInput.perform(replaceText("2024"), closeSoftKeyboard())
        // Hour as 10
        val hourInput = onView(
            allOf(
                withId(R.id.hourInput),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 5),
                isDisplayed()))
        hourInput.perform(replaceText("10"), closeSoftKeyboard())
        // Minute as 30
        val minuteInput = onView(
            allOf(
                withId(R.id.minuteInput),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 7),
                isDisplayed()))
        minuteInput.perform(replaceText("30"), closeSoftKeyboard())
        // Name as Test
        val nameInput = onView(
            allOf(
                withId(R.id.activityNameInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 3),
                isDisplayed()))
        nameInput.perform(replaceText("Test"), closeSoftKeyboard())
        // Check if character counter match
        val charCount = onView(
            allOf(
                withId(R.id.wordCountTextView), withText("4/20"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        charCount.check(matches(withText("4/20")))
        // Duration as 2.5
        val durationInput = onView(
            allOf(
                withId(R.id.durationInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 6),
                isDisplayed()))
        durationInput.perform(replaceText("2.5"), closeSoftKeyboard())
        // CLick Submit button
        val submitButton = onView(
            allOf(
                withId(R.id.submitButton), withText("SUBMIT"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 7),
                isDisplayed()))
        submitButton.perform(click())
        // Now assume if we have been back to Your Activity fragment upon adding exercise item, this should work
        // Check for matching activity item with activityName Test
        val name = onView(
            allOf(
                withId(R.id.activityName), withText("Test"),
                withParent(withParent(withId(R.id.recyclerView))),
                isDisplayed()))
        name.check(matches(withText("Test")))
        // Check for matching activity time with activityTime 18/12/2024 10:30
        val datetime = onView(
            allOf(
                withId(R.id.activityTime), withText("18/12/2024 10:30"),
                withParent(withParent(withId(R.id.recyclerView))),
                isDisplayed()))
        datetime.check(matches(withText("18/12/2024 10:30")))
    }

    // Helper method
    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }
            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
