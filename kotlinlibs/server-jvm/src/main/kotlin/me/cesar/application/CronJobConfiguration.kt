package me.cesar.application

import me.cesar.application.source.Ingestor
import me.cesar.application.storage.Article
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Cron job manager. Use this class to schedule events that should be run with a particular cadence.
 *
 * @author cramsan
 */
@Component
class CronJobConfiguration(
    private val ingestor: Ingestor,
) {

    /**
     * Cron job to start the ingestion process for new [Article].
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    fun processSources() {
        ingestor.processAllSources()
    }
}
