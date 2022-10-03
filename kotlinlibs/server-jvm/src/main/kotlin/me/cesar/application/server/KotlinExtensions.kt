package me.cesar.application.server

import me.cesar.application.common.network.PageResponse
import org.springframework.data.domain.Page

/**
 * Map a Spring [Page] to an instance of cross-platform [PageResponse].
 */
fun <T> Page<T>.toResponse(): PageResponse<T> {
    return PageResponse(
        content = content,
        number = number,
        size = size,
        totalElements = totalElements,
        last = isLast,
        totalPages = totalPages,
        first = isFirst,
        numberOfElements = numberOfElements,
    )
}
