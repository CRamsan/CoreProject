package me.cesar.application.spring.storage.mongodb

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * Configuration bean for storage components for MongoDB
 *
 * @author cramsan
 */
@Suppress("UndocumentedPublicFunction")
@Configuration
@EnableMongoRepositories
class MongoDBStorageConfiguration
