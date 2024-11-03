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
class UpdateActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    // Test all, with each part indicating their purposes
    @Test
    fun updateActivityTestAll() {
        // Part 1: Hardcode an exercise item by navigating to the Add Activity fragment and adding it.
        onView(withId(R.id.addActivityButton)).perform(click())
        Thread.sleep(1000) // Wait for fragment to load
        // Fill in details for the exercise item
        onView(withId(R.id.dayInput)).perform(replaceText("18"))
        onView(withId(R.id.monthInput)).perform(replaceText("12"))
        onView(withId(R.id.yearInput)).perform(replaceText("2024"))
        onView(withId(R.id.hourInput)).perform(replaceText("10"))
        onView(withId(R.id.minuteInput)).perform(replaceText("30"))
        onView(withId(R.id.activityNameInput)).perform(replaceText("Add"))
        onView(withId(R.id.durationInput)).perform(replaceText("1.5"))
        // Check if character counter match
        val charCount1 = onView(
            allOf(
                withId(R.id.wordCountTextView), withText("3/20"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        charCount1.check(matches(withText("3/20")))
        // CLick Submit button
        val submitButton1 = onView(
            allOf(
                withId(R.id.submitButton), withText("SUBMIT"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 7),
                isDisplayed()))
        submitButton1.perform(click())
        // Click Update button on the RecyclerView item
        val updateButton = onView(
            allOf(
                withId(R.id.updateButton), withText("Update"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView), 0), 3),
                isDisplayed()))
        updateButton.perform(click())


        // Part 2: Test UI elements exist and the information provided matches item pre-created
        // Check customized ToolBar widget showing correct fragment (page) name - Update Activity
        val toolBar = onView(
            allOf(
                withText("Update Activity"),
                withParent(
                    allOf(
                        withId(R.id.toolbar),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
                isDisplayed()))
        toolBar.check(matches(withText("Update Activity")))
        // Time text
        val actTimeText = onView(
            allOf(
                withId(R.id.activityTimeText), withText("Activity Time"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        actTimeText.check(matches(isDisplayed()))
        // Day input
        val dayInput = onView(
            allOf(
                withId(R.id.dayInput), withText("18"),
                withParent(
                    allOf(
                        withId(R.id.activityTimeInput),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java)))),
                isDisplayed()))
        dayInput.check(matches(withText("18")))
        // Month input
        val monthInput = onView(
            allOf(
                withId(R.id.monthInput), withText("12"),
                withParent(
                    allOf(
                        withId(R.id.activityTimeInput),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java)))),
                isDisplayed()))
        monthInput.check(matches(withText("12")))
        // Year input
        val yearInput = onView(
            allOf(
                withId(R.id.yearInput), withText("2024"),
                withParent(
                    allOf(
                        withId(R.id.activityTimeInput),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java)))),
                isDisplayed()))
        yearInput.check(matches(withText("2024")))
        // Hour input
        val hourInput = onView(
            allOf(
                withId(R.id.hourInput), withText("10"),
                withParent(
                    allOf(
                        withId(R.id.activityTimeInput),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java)))),
                isDisplayed()))
        hourInput.check(matches(withText("10")))
        val minuteInput = onView(
            allOf(
                withId(R.id.minuteInput), withText("30"),
                withParent(
                    allOf(
                        withId(R.id.activityTimeInput),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java)))),
                isDisplayed()))
        minuteInput.check(matches(withText("30")))
        // Activity Name text
        val actNameText = onView(
            allOf(
                withId(R.id.activityNameText), withText("Activity Name"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        actNameText.check(matches(isDisplayed()))
        // Activity Name input
        val actNameInput = onView(
            allOf(
                withId(R.id.activityNameInput), withText("Add"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        actNameInput.check(matches(withText("Add")))
        // Duration text
        val durText = onView(
            allOf(
                withId(R.id.durationText), withText("Duration"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        durText.check(matches(isDisplayed()))
        // Duration input
        val durInput = onView(
            allOf(
                withId(R.id.durationInput), withText("1.5"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        durInput.check(matches(withText("1.5")))
        // Submit button
        val submitButton2 = onView(
            allOf(
                withId(R.id.submitButton), withText("SUBMIT"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        submitButton2.check(matches(isDisplayed()))
        // Cancel button
        val cancelButton = onView(
            allOf(
                withId(R.id.cancelButton), withText("CANCEL"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        cancelButton.check(matches(isDisplayed()))


        // Part 3: Modify and update new details for the updated item
        // Update Day from 18 to 24
        val fromDay = onView(
            allOf(
                withId(R.id.dayInput), withText("18"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 0),
                isDisplayed()))
        fromDay.perform(replaceText("24"))
        val toDay = onView(
            allOf(
                withId(R.id.dayInput), withText("24"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 0),
                isDisplayed()))
        toDay.perform(closeSoftKeyboard())
        // Update Month from 12 to 02
        val fromMonth = onView(
            allOf(
                withId(R.id.monthInput), withText("12"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)), 2),
                isDisplayed()))
        fromMonth.perform(replaceText("02"))
        val toMonth = onView(
            allOf(
                withId(R.id.monthInput), withText("02"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)), 2),
                isDisplayed()))
        toMonth.perform(closeSoftKeyboard())
        // Update Year from 2024 to 2026
        val fromYear = onView(
            allOf(
                withId(R.id.yearInput), withText("2024"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)), 4),
                isDisplayed()))
        fromYear.perform(replaceText("2026"))
        val toYear = onView(
            allOf(
                withId(R.id.yearInput), withText("2026"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 4),
                isDisplayed()))
        toYear.perform(closeSoftKeyboard())
        // Update Hour from 10 to 21
        val fromHour = onView(
            allOf(
                withId(R.id.hourInput), withText("10"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 5),
                isDisplayed()))
        fromHour.perform(replaceText("21"))
        val toHour = onView(
            allOf(
                withId(R.id.hourInput), withText("21"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)), 5),
                isDisplayed()))
        toHour.perform(closeSoftKeyboard())
        // Update Minute from 30 to 15
        val fromMinute = onView(
            allOf(
                withId(R.id.minuteInput), withText("30"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")), 1)), 7),
                isDisplayed()))
        fromMinute.perform(replaceText("15"))
        val toMinute = onView(
            allOf(
                withId(R.id.minuteInput), withText("15"),
                childAtPosition(
                    allOf(
                        withId(R.id.activityTimeInput),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)), 7),
                isDisplayed()))
        toMinute.perform(closeSoftKeyboard())
        // Update Name from Add to Update
        val fromName = onView(
            allOf(
                withId(R.id.activityNameInput), withText("Add"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 3),
                isDisplayed()))
        fromName.perform(replaceText("Update"))
        val toName = onView(
            allOf(
                withId(R.id.activityNameInput), withText("Update"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 3),
                isDisplayed()))
        toName.perform(closeSoftKeyboard())
        // Check if character counter match
        val charCount2 = onView(
            allOf(
                withId(R.id.wordCountTextView), withText("6/20"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        charCount2.check(matches(withText("6/20")))
        // Update Duration from 1.5 to 2.25
        val fromDur = onView(
            allOf(
                withId(R.id.durationInput), withText("1.5"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 6),
                isDisplayed()))
        fromDur.perform(replaceText("2.25"))
        val toDur = onView(
            allOf(
                withId(R.id.durationInput), withText("2.25"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 6),
                isDisplayed()))
        toDur.perform(closeSoftKeyboard())
        // Click Submit button
        val submitButton3 = onView(
            allOf(
                withId(R.id.submitButton), withText("Submit"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 7),
                isDisplayed()))
        submitButton3.perform(click())


        // Part 4: Check if updated details matches
        // Check updated name details
        val upName = onView(
            allOf(
                withId(R.id.activityName), withText("Update"),
                withParent(withParent(withId(R.id.recyclerView))),
                isDisplayed()))
        upName.check(matches(withText("Update")))
        // Check updated datetime details
        val upDateTime = onView(
            allOf(
                withId(R.id.activityTime), withText("24/02/2026 21:15"),
                withParent(withParent(withId(R.id.recyclerView))),
                isDisplayed()))
        upDateTime.check(matches(withText("24/02/2026 21:15")))
        // Check updated duration
        val upDur = onView(
            allOf(
                withId(R.id.duration), withText("2.25 hours"),
                withParent(withParent(withId(R.id.recyclerView))),
                isDisplayed()))
        upDur.check(matches(withText("2.25 hours")))
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
