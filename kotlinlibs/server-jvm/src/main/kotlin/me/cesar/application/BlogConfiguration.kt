package me.cesar.application

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import me.cesar.application.service.KotlinInstantSerializer
import me.cesar.application.storage.Source
import me.cesar.application.storage.SourceRepository
import me.cesar.application.storage.SourceType
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.Date

/**
 * Provide global site configuration.
 * TODO: Change name of this class.
 *
 * @author cramsan
 */
@Configuration
class BlogConfiguration {

    /**
     * Function that populates the database with [Source] entities at runtime.
     * TODO: This is used for testing purposes only. Once deployed, sources will be persisted in the database.
     */
    @Bean
    fun databaseInitializer(
        sourceRepository: SourceRepository,
    ) = ApplicationRunner {
        sourceRepository.saveAll(ALL_SOURCES)
    }

    /**
     * Define the [ObjectMapper] to be used for this application.
     */
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {

        val kotlinModule = KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()

        kotlinModule.addSerializer(KotlinInstantSerializer())

        return ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(kotlinModule)
    }

    companion object {
        private val KOTLIN_BLOG = Source(
            title = "JetBrains Blog",
            url = "https://blog.jetbrains.com/feed/",
            lastUpdated = Date(0),
            sourceType = SourceType.RSS,
        )

        private val KTOR_BLOG = Source(
            title = "Ktor Blog",
            url = "https://blog.jetbrains.com/ktor/feed/",
            lastUpdated = Date(0),
            sourceType = SourceType.RSS,
        )

        private val KOTLINX_SERIALIZATION_GITHUB_RELEASES = Source(
            title = "Kotlinx Serialization Releases",
            url = "https://github.com/Kotlin/kotlinx.serialization/releases.atom",
            lastUpdated = Date(0),
            sourceType = SourceType.RSS,
        )

        private val KOTLESS_GITHUB_RELEASES = Source(
            title = "Kotless Releases",
            url = "https://github.com/JetBrains/kotless/releases.atom",
            lastUpdated = Date(0),
            sourceType = SourceType.RSS,
        )

        private val KOTLIN_FEED_YOUTUBE = Source(
            title = "Kotlin Youtube Channel",
            url = "https://www.youtube.com/feeds/videos.xml?channel_id=UCP7uiEZIqci43m22KDl0sNw",
            lastUpdated = Date(0),
            sourceType = SourceType.RSS,
        )

        private val ALL_SOURCES = listOf(
            KTOR_BLOG,
            KOTLIN_BLOG,
        )
    }
}
