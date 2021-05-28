package com.cramsan.petproject.webservice

import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerJVM
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
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
    fun eventLogger(): EventLoggerInterface {
        return EventLoggerImpl(Severity.INFO, null, LoggerJVM())
    }

    @Bean
    fun haltUtil(): HaltUtil {
        return HaltUtilImpl(HaltUtilJVM())
    }

    @Bean
    fun assertUtil(
        eventLogger: EventLoggerInterface,
        haltUtil: HaltUtil
    ): AssertUtilInterface {
        return AssertUtilImpl(false, eventLogger, haltUtil)
    }

    @Bean
    fun threadUtil(
        eventLogger: EventLoggerInterface,
        assertUtil: AssertUtilInterface
    ): ThreadUtilInterface {
        return ThreadUtilImpl(ThreadUtilJVM(eventLogger, assertUtil))
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
        return ModelStorage(
            modelStorageDAO,
            eventLogger,
            threadUtil
        )
    }
}
