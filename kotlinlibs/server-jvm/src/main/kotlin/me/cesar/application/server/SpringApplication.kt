package me.cesar.application.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan(
    basePackages = [
        "me.cesar.application.server",
        "me.cesar.application.spring.service",
        "me.cesar.application.spring.storage",
    ],
)
@SpringBootApplication
/**
 * Spring boot application that provides a REST API for fetching data.
 */
class SpringApplication

/**
 * Entry point to start this Spring application.
 */
@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<SpringApplication>(*args)
}
