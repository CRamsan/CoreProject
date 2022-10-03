package me.cesar.application.common.network

/**
 * @author cramsan
 *
 * Kotlin based response for a pageable set of data. The design of this class is based on Spring's Page.
 */
data class PageResponse<T>(
    val content: List<T>,
    val number: Int,
    val size: Int,
    val totalElements: Long?,
    val last: Boolean,
    val totalPages: Int,
    val first: Boolean,
    val numberOfElements: Int,
)
