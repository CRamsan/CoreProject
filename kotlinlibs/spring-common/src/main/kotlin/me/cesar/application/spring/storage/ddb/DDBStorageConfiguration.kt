package me.cesar.application.spring.storage.ddb

import me.cesar.application.common.Environment
import me.cesar.application.common.TableNames
import me.cesar.application.spring.storage.ddb.entity.ArticleEntity
import me.cesar.application.spring.storage.ddb.entity.SourceEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

/**
 * Configuration bean for storage components
 *
 * @author cramsan
 */
@Suppress("UndocumentedPublicFunction")
@Configuration
class DDBStorageConfiguration {

    @Bean
    fun dynamoDbEnhancedClient(): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder().build()
    }

    @Qualifier(TableNames.ARTICLES)
    @Bean
    fun articlesTableName(): String = System.getenv(Environment.ARTICLES_TABLE)

    @Qualifier(TableNames.SOURCES)
    @Bean
    fun sourceTableName(): String = System.getenv(Environment.SOURCES_TABLE)

    @Bean
    fun sourcesTable(
        dynamoDbEnhancedClient: DynamoDbEnhancedClient,
        @Qualifier(TableNames.SOURCES) tableName: String,
    ): DynamoDbTable<SourceEntity> {
        return dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(SourceEntity::class.java),
        )
    }

    @Bean
    fun articlesTable(
        dynamoDbEnhancedClient: DynamoDbEnhancedClient,
        @Qualifier(TableNames.ARTICLES) tableName: String,
    ): DynamoDbTable<ArticleEntity> {
        return dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(ArticleEntity::class.java),
        )
    }
}
