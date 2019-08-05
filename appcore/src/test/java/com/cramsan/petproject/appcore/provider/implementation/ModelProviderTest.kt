package com.cramsan.petproject.appcore.provider.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import java.util.concurrent.Semaphore
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
        modelProviderTest.setUp(ModelProviderPlatformInitializer())
        semaphore = Semaphore(0)
    }

    @Test
    fun testNoop() {
        modelProviderTest.testNoop()
    }

    @After
    fun tearDown() {
        modelProviderTest.tearDown()
    }
}
