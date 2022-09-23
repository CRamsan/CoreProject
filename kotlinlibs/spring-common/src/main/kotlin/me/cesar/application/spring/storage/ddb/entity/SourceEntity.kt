package me.cesar.application.spring.storage.ddb.entity

import me.cesar.application.common.AttributeNames
import me.cesar.application.common.model.SourceType
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

/**
 * Entity class to persist a source in the database.
 * The source represents a location that can be accessed to retrieve new content.
 *
 * @author cramsan
 */
@DynamoDbBean
class SourceEntity(
    @get:DynamoDbAttribute(AttributeNames.ID)
    @get:DynamoDbSortKey
    var id: String? = null,
    var title: String = "",
    var url: String = "",
    @get:DynamoDbAttribute(AttributeNames.LAST_UPDATED)
    var lastUpdated: Long? = null,
    @get:DynamoDbAttribute(AttributeNames.SOURCE_TYPE)
    @get:DynamoDbPartitionKey
    var sourceType: String? = null,
) {
    companion object {
        /**
         * Key used to identify this entity in a DDB table.
         */
        data class Key(val sourceType: SourceType, val id: String?)
    }
}
