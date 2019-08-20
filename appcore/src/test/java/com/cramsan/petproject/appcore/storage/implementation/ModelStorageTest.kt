package com.cramsan.petproject.appcore.storage.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.util.concurrent.Semaphore
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ModelStorageTest {

    private lateinit var modelStorageTest: ModelStorageCommonTest
    private lateinit var semaphore: Semaphore

    @Before
    fun setUp() {
        modelStorageTest = ModelStorageCommonTest()
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        modelStorageTest.setUp(ModelStoragePlatformInitializer(appContext))
        semaphore = Semaphore(0)
    }

    @Test
    fun passTest() {
    }
}
