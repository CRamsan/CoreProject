package me.cesar.application.frontend

import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import me.cesar.application.common.model.Article
import me.cesar.application.common.network.PageResponse

/**
 * Dummy class for testing.
 *
 * @author cramsan
 */
class DummyAPIClientImpl : APIClient {
    override suspend fun getArticle(articleId: String): Article {
        delay(1000)
        return generateArticle("1")
    }

    override suspend fun getArticles(sourceId: String): PageResponse<Article> {
        delay(1000)
        val list = (0..15).map {
            generateArticle(it.toString())
        }
        return PageResponse(
            content = list,
            number = list.size,
            size = list.size,
            totalElements = list.size.toLong(),
            last = false,
            totalPages = 3,
            first = true,
            numberOfElements = list.size,
        )
    }

    private fun generateArticle(id: String): Article {
        return Article(
            id = id,
            title = "Title $id",
            sourceId = "324423234324",
            content = "This is some text - $id",
            bannerUrl = "https://blog.jetbrains.com/wp-content/uploads/" +
                "2022/09/Featured_image_1280x600_EduTools.png",
            publishedTime = Instant.fromEpochSeconds(100),
        )
    }
}
