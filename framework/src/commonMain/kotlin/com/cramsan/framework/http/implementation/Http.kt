package com.cramsan.framework.http.implementation

import com.cramsan.framework.http.HttpEngine
import com.cramsan.framework.http.HttpInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.ThreadUtilInterface
import kotlin.reflect.KClass

class Http(
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface,
    initializer: HttpInitializer
) : HttpInterface {

    private val engine: HttpEngine = initializer.httpEngine

    override fun <T> get(url: String, callback: T) {
        eventLogger.log(Severity.INFO, classTag(), "Calling get with callback")
        threadUtil.assertIsBackgroundThread()
        return engine.get(url, callback)
    }

    override fun <T> getBlocking(url: String): T {
        eventLogger.log(Severity.INFO, classTag(), "Calling get as blocking")
        threadUtil.assertIsBackgroundThread()
        return engine.getBlocking<T>(url)
    }

    override suspend fun <T : Any> get(url: String, c: KClass<T>): T {
        eventLogger.log(Severity.INFO, classTag(), "Calling get as coroutine")
        threadUtil.assertIsBackgroundThread()
        return engine.get(url, c)
    }
}
