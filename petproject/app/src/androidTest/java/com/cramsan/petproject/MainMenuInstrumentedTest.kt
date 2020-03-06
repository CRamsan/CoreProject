package com.cramsan.petproject

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.cramsan.petproject.mainmenu.AllPlantListViewModel
import com.cramsan.petproject.mainmenu.DownloadCatalogViewModel
import com.cramsan.petproject.mainmenu.MainMenuActivity
import io.mockk.every
import io.mockk.mockkClass
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.factory

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainMenuInstrumentedTest {

    @get:Rule
    val activityScenarioRule = ActivityTestRule(MainMenuActivity::class.java,
        false,
        false)

    @Before
    fun before() {
    }

    @After
    fun after() {
        activityScenarioRule.finishActivity()
    }

    @Test
    fun loadingScreenOnFirstLoad() {
        val viewModelProvider = mockkClass(ViewModelProvider::class)
        val downloadCatalogViewModel = mockkClass(DownloadCatalogViewModel::class)
        val allPlantListViewModel = mockkClass(AllPlantListViewModel::class)

        every { viewModelProvider.get(DownloadCatalogViewModel::class.java) } returns downloadCatalogViewModel
        every { viewModelProvider.get(AllPlantListViewModel::class.java) } returns allPlantListViewModel

        Kodein.lazy {
            bind<ViewModelProvider>() with factory {
                    _: ViewModelStoreOwner -> viewModelProvider
            }
        }

        val activity = activityScenarioRule.launchActivity(null)
        activity.viewModelProvider = viewModelProvider

        onView(withId(R.id.download_dialog_activity)).check(matches(isDisplayed()))
    }
}
