package com.cesarandres.ps2link

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.microsoft.appcenter.espresso.Factory
import com.microsoft.appcenter.espresso.ReportHelper
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
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
class FragmentServersTest {

    @get:Rule
    val activityScenarioRule = ActivityTestRule(ActivityContainer::class.java, false, false)

    @get:Rule
    var reportHelper: ReportHelper = Factory.getReportHelper()

    val defaultProfile = "CRamsan"

    companion object {
        @BeforeClass
        fun setup() {
            clearAppData(getInstrumentation().targetContext)
        }
    }

    @Before
    fun before() {
        clearPreferences(getInstrumentation().targetContext)
        clearAppData(getInstrumentation().targetContext)
        activityScenarioRule.launchActivity(null)
        val application = activityScenarioRule.activity.application as ApplicationPS2Link
        // IdlingRegistry.getInstance().register(application.idlingResource)
    }

    @After
    fun after() {
        val application = activityScenarioRule.activity.application as ApplicationPS2Link
        // IdlingRegistry.getInstance().unregister(application.idlingResource)
        reportHelper.label("Stopping App")
    }

    @Test
    fun openServerListChangeNamespace() {
        /*
        onView(withId(R.id.buttonPreferedProfile)).check(matches(not(isDisplayed())))
        onView(withId(R.id.buttonPreferedOutfit)).check(matches(not(isDisplayed())))

        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonFragmentStar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonShowOffline)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonWeapons)).check(matches(not(isDisplayed())))

        onView(withId(R.id.buttonServers)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewServers)).atPosition(5)
            .perform(click())
        onView(withId(R.id.buttonTitle)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewServers)).atPosition(0)
            .perform(click())
        onView(withId(R.id.buttonTitle)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewServers)).atPosition(0)
            .perform(click())
        onView(withId(R.id.buttonTitle)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewServers)).atPosition(5)
            .perform(click())
         */
    }

    @Test
    fun openServerListUpdate() {
        /*
        onView(withId(R.id.buttonPreferedProfile)).check(matches(not(isDisplayed())))
        onView(withId(R.id.buttonPreferedOutfit)).check(matches(not(isDisplayed())))

        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonFragmentStar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonShowOffline)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonWeapons)).check(matches(not(isDisplayed())))

        onView(withId(R.id.buttonServers)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewServers)).atPosition(5)
            .perform(click())
        onView(withId(R.id.buttonFragmentUpdate)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewServers)).atPosition(5)
            .perform(click())
         */
    }
}
