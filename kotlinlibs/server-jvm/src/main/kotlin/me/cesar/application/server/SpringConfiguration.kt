package me.cesar.application.server

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.datetime.Clock
import me.cesar.application.spring.createObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Provide bean management for the [SpringApplication].
 *
 * @author cramsan
 */
@Configuration
class SpringConfiguration {

    /**
     * Provide a [Clock] instance to be used for time management.
     */
    @Bean
    fun clock(): Clock = Clock.System

    /**
     * Provide an [ObjectMapper] that is customized to support all the required serialization and deserialization
     * needs.
     */
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = createObjectMapper()
}
