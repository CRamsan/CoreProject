package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.newInstance

class HaltUtilCommonTest {

    private val kodein = Kodein {
        bind<EventLoggerInterface>() with provider { mockk<EventLogger>() }
    }

    private lateinit var haltUtil: HaltUtilInterface
    private lateinit var haltUtilImpl: HaltUtil

    fun setUp() {
        val initializer = HaltUtilInitializer()
        val newHaltUtil by kodein.newInstance { HaltUtil(initializer, instance()) }
        haltUtilImpl = newHaltUtil
        haltUtil = haltUtilImpl
    }

    suspend fun testStopThread() = coroutineScope {
        launch(Dispatchers.Default) {
            delay(2000)
            haltUtilImpl.resumeThread()
        }
        launch(Dispatchers.Main) {
            haltUtil.stopThread()
        }
    }

    fun testStopMainThread() {
        // haltUtil.stopMainThread()
    }

    fun testCrashApp() {
        // haltUtil.crashApp()
    }
}
