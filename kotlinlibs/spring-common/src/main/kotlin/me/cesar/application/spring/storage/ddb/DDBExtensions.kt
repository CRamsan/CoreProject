package me.cesar.application.spring.storage.ddb

import me.cesar.application.spring.storage.ddb.entity.ArticleEntity
import me.cesar.application.spring.storage.ddb.entity.SourceEntity
import software.amazon.awssdk.enhanced.dynamodb.Key

/**
 * Helper class to convert an [ArticleEntity.ArticleIdKey] to a [Key].
 */
fun ArticleEntity.Companion.ArticleIdKey.toKey(): Key {
    val builder = Key.builder().apply {
        partitionValue(id)
    }
    return builder.build()
}

/**
 * Helper class to convert an [ArticleEntity.ArticleSourceKey] to a [Key].
 */
fun ArticleEntity.Companion.ArticleSourceKey.toKey(): Key {
    val builder = Key.builder().apply {
        partitionValue(source)
    }
    return builder.build()
}

/**
 * Helper class to convert an [SourceEntity.Key] to a [Key].
 */
fun SourceEntity.Companion.Key.toKey(): Key {
    val builder = Key.builder().apply {
        partitionValue(sourceType.name)
        if (id != null) {
            sortValue(id)
        }
    }
    return builder.build()
}
