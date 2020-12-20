package com.cesarandres.ps2link

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.microsoft.appcenter.espresso.Factory
import com.microsoft.appcenter.espresso.ReportHelper
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
class FragmentProfileTest {

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
    fun openSearchProfileSetPreferred() {
        /*
        onView(withId(R.id.buttonPreferedProfile)).check(matches(not(isDisplayed())))
        onView(withId(R.id.buttonPreferedOutfit)).check(matches(not(isDisplayed())))

        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonFragmentStar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonShowOffline)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonWeapons)).check(matches(not(isDisplayed())))

        onView(withId(R.id.buttonCharacters)).perform(click())
        onView(withId(R.id.buttonFragmentAdd)).perform(click())
        onView(withId(R.id.fieldSearchProfile)).perform(typeText(defaultProfile.toLowerCase()), closeSoftKeyboard())
        onView(withId(R.id.imageButtonSearchProfile)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listFoundProfiles)).atPosition(0).perform(click())

        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
        onView(withId(R.id.toggleButtonFragmentStar)).check(matches(isNotChecked())).perform(
            scrollTo(),
            click()
        )
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.pressBack()

        onView(withId(R.id.buttonPreferedProfile)).perform(click())
        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
         */
    }

    @Test
    fun openSearchProfileCache() {
        /*
        onView(withId(R.id.buttonPreferedProfile)).check(matches(not(isDisplayed())))
        onView(withId(R.id.buttonPreferedOutfit)).check(matches(not(isDisplayed())))

        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonFragmentStar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonShowOffline)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonWeapons)).check(matches(not(isDisplayed())))

        onView(withId(R.id.buttonCharacters)).perform(click())
        onView(withId(R.id.buttonFragmentAdd)).perform(click())
        onView(withId(R.id.fieldSearchProfile)).perform(typeText(defaultProfile.toLowerCase()), closeSoftKeyboard())
        onView(withId(R.id.imageButtonSearchProfile)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listFoundProfiles)).atPosition(0).perform(click())

        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
        onView(withId(R.id.fragmentTitleLoading)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(isNotChecked())).perform(click())
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.pressBack()

        onView(withId(R.id.buttonCharacters)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewProfileList)).atPosition(0).perform(click())
        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
         */
    }

    @Test
    fun openSearchMultiNamespaceProfileCache() {
        /*
        onView(withId(R.id.buttonPreferedProfile)).check(matches(not(isDisplayed())))
        onView(withId(R.id.buttonPreferedOutfit)).check(matches(not(isDisplayed())))

        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonFragmentStar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonShowOffline)).check(matches(not(isDisplayed())))
        onView(withId(R.id.toggleButtonWeapons)).check(matches(not(isDisplayed())))

        onView(withId(R.id.buttonCharacters)).perform(click())
        onView(withId(R.id.buttonFragmentAdd)).perform(click())
        onView(withId(R.id.fieldSearchProfile)).perform(typeText(defaultProfile.toLowerCase()), closeSoftKeyboard())
        onView(withId(R.id.imageButtonSearchProfile)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listFoundProfiles)).atPosition(0).perform(click())

        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
        onView(withId(R.id.fragmentTitleLoading)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(isNotChecked())).perform(click())

        Espresso.pressBack()
        onView(withId(R.id.buttonTitle)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listFoundProfiles)).atPosition(0).perform(click())

        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
        onView(withId(R.id.fragmentTitleLoading)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.toggleButtonFragmentAppend)).check(matches(isNotChecked())).perform(click())

        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.pressBack()

        onView(withId(R.id.buttonCharacters)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listViewProfileList)).atPosition(1).perform(click())
        onView(withId(R.id.buttonFragmentTitle)).check(matches(withText(defaultProfile)))
         */
    }
}
