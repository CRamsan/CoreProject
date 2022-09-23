package me.cesar.application.spring.storage.ddb

import me.cesar.application.common.model.Article
import me.cesar.application.spring.storage.ArticleRepository
import me.cesar.application.spring.storage.ddb.entity.ArticleEntity
import me.cesar.application.spring.toModel
import me.cesar.application.spring.toStorage
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch

/**
 * CRUD repository for [Article] entities. This implementation allows for pagination.
 *
 * @author cramsan
 */
@Repository
class DDBArticleRepository(
    private val table: DynamoDbTable<ArticleEntity>,
    private val ddbClient: DynamoDbEnhancedClient,
) : ArticleRepository {

    private val singleArticleIndex by lazy { table.index("singleArticle") }

    private val articlesByLastUpdatedForSourceIndex by lazy { table.index("articlesByLastUpdated") }

    override fun findArticle(id: String): Result<Article> = runCatching {
        val key = ArticleEntity.Companion.ArticleIdKey(
            id,
        ).toKey()
        val result = singleArticleIndex.query(QueryConditional.keyEqualTo(key))
        result.asIterable().first().items().first().toModel()
    }

    override fun insert(article: Article) = runCatching {
        table.putItem(article.toStorage())
    }

    override fun insert(articles: List<Article>) = runCatching {
        val entities = articles.map { it.toStorage() }
        ddbClient.batchWriteItem {
            val batch = WriteBatch.builder(ArticleEntity::class.java).apply {
                mappedTableResource(table)
                entities.forEach { article ->
                    addPutItem(article)
                }
            }.build()
            it.writeBatches(batch)
        }
        Unit
    }

    override fun findAll(): Result<List<Article>> = runCatching {
        val result = articlesByLastUpdatedForSourceIndex.scan { }
        result.asIterable().first().items().map { it.toModel() }
    }

    override fun findAll(sourceId: String): Result<List<Article>> = runCatching {
        val key = ArticleEntity.Companion.ArticleSourceKey(
            sourceId,
        ).toKey()
        val result = articlesByLastUpdatedForSourceIndex.query(QueryConditional.keyEqualTo(key))
        result.asIterable().first().items().map { it.toModel() }
    }
}
