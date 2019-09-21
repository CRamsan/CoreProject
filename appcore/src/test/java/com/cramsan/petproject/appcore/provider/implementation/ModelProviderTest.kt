package com.cramsan.petproject.appcore.provider.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.preferences.implementation.PlatformPreferences
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import java.util.concurrent.Semaphore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.erased.provider

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ModelProviderTest {

    private lateinit var modelProviderTest: ModelProviderCommonTest
    private lateinit var semaphore: Semaphore

    @Before
    fun setUp() {
        modelProviderTest = ModelProviderCommonTest()
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        modelProviderTest.setUp(ModelStoragePlatformInitializer(appContext), PlatformPreferences(appContext))
        semaphore = Semaphore(0)
    }

    @Test
    fun testFiltering() {
        runBlocking {
            launch(Dispatchers.IO) {
                modelProviderTest.testFiltering()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }
}
