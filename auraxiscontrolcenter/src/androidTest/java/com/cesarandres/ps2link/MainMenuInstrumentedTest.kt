package com.cesarandres.ps2link

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainMenuInstrumentedTest {

    @get:Rule
    val activityScenarioRule = ActivityTestRule(ActivityContainer::class.java)

    @Before
    fun before() {
    }

    @After
    fun after() {
    }

    @Test
    fun switchBackgrounds() {
        onView(withId(R.id.buttonNC)).perform(click())
        allowPermissionsIfNeeded(WRITE_EXTERNAL_STORAGE)
        onView(withId(R.id.buttonTR)).perform(click())
        onView(withId(R.id.buttonVS)).perform(click())
        onView(withId(R.id.buttonPS2)).perform(click())
    }

    @Test
    fun openProfile() {
        onView(withId(R.id.buttonCharacters)).perform(click())
    }

    @Test
    fun openServers() {
        onView(withId(R.id.buttonServers)).perform(click())
    }

    @Test
    fun openOutfits() {
        onView(withId(R.id.buttonOutfit)).perform(click())
    }

    @Test
    fun openNews() {
        onView(withId(R.id.buttonNews)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun openSettings() {
        onView(withId(R.id.buttonSettings)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun openTwitter() {
        onView(withId(R.id.buttonTwitter)).perform(click())
    }

    @Test
    fun openReddit() {
        onView(withId(R.id.buttonRedditFragment)).perform(click())
    }

    @Test
    fun openAbout() {
        onView(withId(R.id.buttonAbout)).perform(click())
    }
}
