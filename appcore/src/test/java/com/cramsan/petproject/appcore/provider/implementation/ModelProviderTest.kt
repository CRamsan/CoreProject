package com.cramsan.petproject.appcore.provider.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.framework.implementation.MockCoreFramework
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageCommonTest
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Semaphore

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ModelProviderTest {

    private lateinit var modelProviderTest: ModelProviderCommonTest
    private lateinit var semaphore: Semaphore

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            CoreFrameworkAPI.init(MockCoreFramework())
            CoreFrameworkAPI.initEventLogger(Severity.VERBOSE)
            CoreFrameworkAPI.initThreadUtil()
            CoreFrameworkAPI.initHaltUtil()
            val appContext = ApplicationProvider.getApplicationContext<Context>()
            CoreFrameworkAPI.initModelStorage(ModelStoragePlatformInitializer(appContext))
        }
    }

    @Before
    fun setUp() {
        modelProviderTest = ModelProviderCommonTest()
        modelProviderTest.setUp(ModelProviderPlatformInitializer())
        semaphore = Semaphore(0)
    }

    @After
    fun tearDown() {
        modelProviderTest.tearDown()
    }
}
