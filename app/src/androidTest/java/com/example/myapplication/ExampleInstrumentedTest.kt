package com.example.myapplication
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<HomeActivity> =
        ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun loginAndCheckRecyclerView() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
        onView(withId(R.id.edittext_email)).perform(typeText("andersjorgensen400@gmail.com"), closeSoftKeyboard(), pressImeActionButton())
        onView(withId(R.id.edittext_password)).perform(typeText("1Frankrig3"), closeSoftKeyboard(), pressImeActionButton())
        onView(withId(R.id.button_login)).perform(click())
    }

}