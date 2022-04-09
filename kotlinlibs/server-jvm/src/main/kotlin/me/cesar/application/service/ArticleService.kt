package me.cesar.application.service

import me.cesar.application.model.Article
import me.cesar.application.storage.ArticleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * Internal service to control how to expose access to the [Article] instances.
 *
 * @author cramsan
 */
@Service
class ArticleService(
    private val repository: ArticleRepository,
) {
    /**
     * Provide a paginated list of [Article]. The return is of type [Page]. The [page], [count] and [sortBy] can be used
     * to control the data fetched.
     */
    fun findAllByOrderByPublishedAtDesc(
        page: Int,
        count: Int,
        sortBy: String,
    ): Page<Article> {
        val paging = PageRequest.of(page, count, Sort.by(sortBy))

        return repository.findAllByOrderByPublishedAtDesc(paging).map { it.toModel() }
    }

    /**
     * Fetch a single [Article] with [id]. If no match is found, return null.
     */
    fun findById(
        id: Long,
    ): Article? {
        return repository.findById(id).orElseGet { null }.toModel()
    }
}
