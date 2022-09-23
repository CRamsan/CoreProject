package me.cesar.application.lambda

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan(
    basePackages = [
        "me.cesar.application.lambda",
        "me.cesar.application.spring.service",
        "me.cesar.application.spring.storage",
    ],
)
@SpringBootApplication
/**
 * Spring class that will run the lamba code. This application is in charge of managing the ingestion of articles from
 * internet sources.
 */
class LambdaApplication

@Suppress("SpreadOperator", "UndocumentedPublicFunction")
fun main(args: Array<String>) {
    runApplication<LambdaApplication>(*args)
}
