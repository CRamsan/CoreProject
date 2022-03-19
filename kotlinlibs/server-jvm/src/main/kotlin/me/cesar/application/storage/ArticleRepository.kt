package me.cesar.application.storage

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * CRUD repository for [Article] entities. This implementation allow for pagination.
 *
 * @author cramsan
 */
interface ArticleRepository : PagingAndSortingRepository<Article, Long> {

    /**
     * Function to retrieve all entities sorted by the "publishedAt" value.
     * The response is paginated.
     */
    fun findAllByOrderByPublishedAtDesc(pageable: Pageable): Page<Article>
}
