package com.cramsan.ps2link.appcore.repository

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.core.models.RedditPage
import com.cramsan.ps2link.core.models.RedditPost
import com.cramsan.ps2link.network.models.reddit.RedditResponse
import io.ktor.http.Url

class RedditRepositoryImpl(
    private val http: HttpClient,
) : RedditRepository {

    override suspend fun getPosts(redditPage: RedditPage): List<RedditPost> {
        logI(TAG, "Downloading Reddit Posts")
        val url = "$BASE_URL/${redditPage.path}/$JSON_ENDPOINT"
        val body = http.sendRequestWithRetry<RedditResponse>(Url(url))
        return body?.data?.children?.map { it.toCoreModel() } ?: emptyList()
    }

    companion object {
        const val TAG = "RedditRepositoryImpl"
        const val BASE_URL = "https://www.reddit.com"
        const val JSON_ENDPOINT = "hot.json"
    }
}
