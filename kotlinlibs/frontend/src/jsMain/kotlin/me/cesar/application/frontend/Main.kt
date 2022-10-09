package me.cesar.application.frontend

import kotlinx.browser.window
import me.cesar.application.frontend.article.articlePage
import me.cesar.application.frontend.home.homePage
import org.w3c.dom.url.URLSearchParams

/**
 * Entry point for the Kotlin Libs Page
 */
fun main() {
    val articleId = URLSearchParams(window.location.search).get("articleId")

    console.log("Starting app with id $articleId")

    if (articleId.isNullOrBlank()) {
        homePage()
    } else {
        articlePage(articleId)
    }
}
