package com.cramsan.ps2link.appcore.repository

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.appcore.census.UrlHolder
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.appcore.network.process
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.core.models.RedditPage
import com.cramsan.ps2link.core.models.RedditPost
import com.cramsan.ps2link.metric.HttpNamespace
import com.cramsan.ps2link.network.models.reddit.RedditResponse
import io.ktor.http.Url

class RedditRepositoryImpl(
    private val http: HttpClient,
) : RedditRepository {

    override suspend fun getPosts(redditPage: RedditPage): PS2HttpResponse<List<RedditPost>> {
        logI(TAG, "Downloading Reddit Posts")
        val url = "$BASE_URL/${redditPage.path}/$JSON_ENDPOINT"
        val response = http.sendRequestWithRetry<RedditResponse>(
            UrlHolder(
                HttpNamespace.Api.REDDIT,
                Url(url)
            )
        )

        return response.process {
            it.data.children.map { post -> post.data.toCoreModel() }
        }
    }

    companion object {
        const val TAG = "RedditRepositoryImpl"
        const val BASE_URL = "https://www.reddit.com"
        const val JSON_ENDPOINT = "hot.json"
    }
}
