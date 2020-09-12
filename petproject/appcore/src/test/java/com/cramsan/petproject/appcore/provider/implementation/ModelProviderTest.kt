package com.cramsan.petproject.appcore.provider.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageAndroidProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Semaphore

/**
 * Unit test. This will be executed in a mocked Android environment.
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
        modelProviderTest.setUp()
        semaphore = Semaphore(0)
    }

    @Test
    fun testIsCatalogAvailable() = runBlocking {
        modelProviderTest.testIsCatalogAvailable()
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
