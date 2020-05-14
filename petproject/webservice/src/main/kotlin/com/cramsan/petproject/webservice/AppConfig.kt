package com.cramsan.petproject.webservice

import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.LoggerJVM
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.Metrics
import com.cramsan.framework.metrics.implementation.MetricsErrorCallback
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilJVM
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageJdbcProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class AppConfig {

    @Bean
    fun metrics(): MetricsInterface {
        return Metrics(object : MetricsInterface {
            override fun initialize() {}
            override fun log(tag: String, event: String) {}
            override fun log(tag: String, event: String, metadata: Map<String, String>) {}
        })
    }

    @Bean
    fun errorCallback(metrics: MetricsInterface): EventLoggerErrorCallbackInterface {
        return MetricsErrorCallback(metrics)
    }

    @Bean
    fun eventLogger(errorCallback: EventLoggerErrorCallbackInterface): EventLoggerInterface {
        return EventLogger(Severity.INFO, errorCallback, LoggerJVM())
    }

    @Bean
    fun haltUtil(): HaltUtilInterface {
        return HaltUtil(HaltUtilJVM())
    }

    @Bean
    fun assertUtil(
        eventLogger: EventLoggerInterface,
        haltUtil: HaltUtilInterface
    ): AssertUtilInterface {
        return AssertUtil(false, eventLogger, haltUtil)
    }

    @Bean
    fun threadUtil(
        eventLogger: EventLoggerInterface,
        assertUtil: AssertUtilInterface
    ): ThreadUtilInterface {
        return ThreadUtil(ThreadUtilJVM(eventLogger, assertUtil))
    }

    @Bean
    fun modelStorage(
        eventLogger: EventLoggerInterface,
        threadUtil: ThreadUtilInterface
    ): ModelStorageInterface {
        val resource = ClassPathResource("PetProject.sql")
        if (!resource.isFile || !resource.exists()) {
            throw UnsupportedOperationException("File not found")
        }
        val dbPath: String? = resource.uri.path
        if (dbPath == null) {
            throw UnsupportedOperationException("Path for sqlite is null")
        }
        val modelStorageDAO = ModelStorageJdbcProvider(
            dbPath
        ).provide()
        return ModelStorage(modelStorageDAO,
            eventLogger,
            threadUtil)
    }
}
