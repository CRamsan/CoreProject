package me.cesar.application.frontend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import me.cesar.application.common.InstantAsLongSerializer
import me.cesar.application.common.model.Article
import me.cesar.application.common.network.PageResponse

/**
 * Ktor client to fetch data from the API.
 *
 * @author cramsan
 */
class KtorAPIClient : APIClient {

    private val json = Json {
        SerializersModule {
            contextual(InstantAsLongSerializer)
        }
    }

    private val client = HttpClient(Js) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    override suspend fun getArticle(articleId: String): Article {
        val response = client.get("http://localhost:5000/api/article/6341adfd16301d774753b581")
        return response.body()
    }

    override suspend fun getArticles(sourceId: String): PageResponse<Article> {
        val response = client.get("http://localhost:5000/api/article/")
        return response.body()
    }
}
