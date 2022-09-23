package me.cesar.application.spring.storage.ddb.entity

import me.cesar.application.common.AttributeNames
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

/**
 * Entity for storing the information about a single article.
 *
 * @author cramsan
 */
@DynamoDbBean
class ArticleEntity(
    @get:DynamoDbAttribute(AttributeNames.ID)
    @get:DynamoDbSortKey
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["singleArticle"])
    var id: String? = null,
    var title: String? = null,
    @get:DynamoDbAttribute(AttributeNames.SOURCE)
    @get:DynamoDbPartitionKey
    var source: String? = null,
    var content: String? = null,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["articlesByLastUpdated"])
    @get:DynamoDbAttribute(AttributeNames.LAST_UPDATED)
    var lastUpdated: Long? = null,
) {

    companion object {
        /**
         * Key used to identify this entity in a DDB table.
         */
        data class Key(val source: String, val id: String?)

        /**
         * Key that uses a GSI to access a single [ArticleEntity].
         */
        data class ArticleIdKey(val id: String)

        /**
         * Key that uses a GSI to query articles
         */
        data class ArticleSourceKey(val source: String)
    }
}
