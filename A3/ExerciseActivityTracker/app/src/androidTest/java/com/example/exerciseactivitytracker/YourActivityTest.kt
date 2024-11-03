package com.example.exerciseactivitytracker


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class YourActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    // Test UI elements exist
    @Test
    fun UIElementExist() {
        // Check customized ToolBar widget showing correct fragment (page) name - Your Activity
        val toolBar = onView(
            allOf(
                withText("Your Activity"),
                withParent(allOf (withId(R.id.toolbar),
                withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
                isDisplayed()))
        toolBar.check(matches(withText("Your Activity")))
        // Check Date-Night toggle theme button exist
        val DNButton = onView(
            allOf(
                withId(R.id.day_night_toggle), withContentDescription("Toggle Theme"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()))
        DNButton.check(matches(isDisplayed()))
        // Check Filter by Date button exist
        val FBDButton = onView(
            allOf(
                withId(R.id.filterByDateButton), withText("FILTER BY DATE"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        FBDButton.check(matches(isDisplayed()))
        // Check Add Activity button exist
        val addButton = onView(
            allOf(
                withId(R.id.addActivityButton), withText("ADD ACTIVITY"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        addButton.check(matches(isDisplayed()))
        // Check exercise RecyclerView items exist
        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerView),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()))
        recyclerView.check(matches(isDisplayed()))
    }

    // Check if Filter by Date button open DatePicker dialog
    @Test
    fun FilterByDateButtonOpenDatePickerDialog() {
        val FBDbutton = onView(
            allOf(
                withId(R.id.filterByDateButton), withText("Filter by Date"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 0),
                isDisplayed()))
        FBDbutton.perform(click())
        // Assert DatePicker widget shown
        onView(withClassName(equalTo("android.widget.DatePicker"))).check(matches(isDisplayed()))
    }

    // Test Add Activity Fragment navigation
    @Test
    fun AddActivityButtonOpenAddActivityFragment() {
        val addButton = onView(
            allOf(
                withId(R.id.addActivityButton), withText("Add Activity"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment), 0), 1),
                isDisplayed()))
        addButton.perform(click())
        // Check customized ToolBar widget showing correct fragment (page) name - Add Activity
        val toolBar = onView(
            allOf(
                withText("Add Activity"),
                withParent(allOf (withId(R.id.toolbar),
                    withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
                isDisplayed()))
        toolBar.check(matches(withText("Add Activity")))
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
