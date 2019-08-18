package com.cramsan.petproject.webservice

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.EventLoggerInitializer
import com.cramsan.framework.logging.implementation.PlatformLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun eventLogger(): EventLoggerInterface {
        return EventLogger(EventLoggerInitializer(Severity.INFO, PlatformLogger()))
    }

    @Bean
    fun threadUtil(): ThreadUtilInterface {
        return ThreadUtil(eventLogger())
    }

    @Bean
    fun haltUtil(): HaltUtilInterface {
        return HaltUtil(eventLogger())
    }

    @Bean
    fun modelStorage(): ModelStorageInterface {
        return ModelStorage(
                ModelStorageInitializer(ModelStoragePlatformInitializer()),
                eventLogger(),
                threadUtil())
    }
}
