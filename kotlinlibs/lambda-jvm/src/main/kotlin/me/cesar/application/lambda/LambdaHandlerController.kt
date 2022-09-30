package me.cesar.application.lambda

import me.cesar.application.common.network.IngestionResult
import me.cesar.application.common.network.InsertionRequest
import me.cesar.application.spring.service.SourceService
import me.cesar.application.spring.service.ingestion.IngestorService
import me.cesar.application.spring.toSource
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import java.util.function.Function

/**
 * The controller that will provide the functionality to handle cloud functions.
 */
@Controller
class LambdaHandlerController(
    private val ingestor: IngestorService,
    private val sourceService: SourceService,
) {

    /**
     * Function to trigger the process of ingestion for all sources.
     */
    @Bean
    fun triggerIngestion(): Function<String, IngestionResult> = Function {
        ingestor.processAllSources().getOrThrow()
    }

    /**
     * Function to that intakes an [InsertionRequest] to add a source and then fetches articles from that source.
     */
    @Bean
    fun insertSource(): Function<InsertionRequest, IngestionResult> = Function {
        val source = it.toSource()
        val result = sourceService.insert(source).getOrThrow()
        ingestor.processSingleSources(result).getOrThrow()
    }
}
