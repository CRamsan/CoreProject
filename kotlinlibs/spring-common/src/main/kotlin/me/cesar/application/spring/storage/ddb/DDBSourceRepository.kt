package me.cesar.application.spring.storage.ddb

import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.storage.SourceRepository
import me.cesar.application.spring.storage.ddb.entity.SourceEntity
import me.cesar.application.spring.toModel
import me.cesar.application.spring.toStorage
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional

/**
 * CRUD repository for [SourceEntity] entities. This implementation allows for pagination.
 *
 * @author cramsan
 */
@Repository
class DDBSourceRepository(
    private val table: DynamoDbTable<SourceEntity>,
) : SourceRepository {

    override fun findSource(
        id: String,
        sourceType: SourceType,
    ): Result<Source> = runCatching {
        val key = SourceEntity.Companion.Key(sourceType, id)
        table.getItem(key.toKey()).toModel()
    }

    override fun insert(source: Source) = runCatching {
        val entity = source.toStorage()
        table.putItem(entity)
        entity.toModel()
    }

    override fun findAll(sourceType: SourceType) = runCatching {
        val key: Key = SourceEntity.Companion.Key(
            sourceType,
            null,
        ).toKey()
        val result = table.query(QueryConditional.keyEqualTo(key))

        result.items().map { it.toModel() }
    }

    override fun findAll(): Result<List<Source>> = runCatching {
        table.scan().items().map { it.toModel() }
    }
}
