package me.cesar.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
/**
 * Spring boot application.
 */
class DemoApplication

/**
 * Entry point to start this Spring application.
 */
@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
