package me.cesar.application.spring

import com.cramsan.framework.utils.uuid.UUID
import kotlinx.datetime.Instant
import me.cesar.application.common.model.Source
import me.cesar.application.common.network.InsertionRequest

/**
 * Transform from an [InsertionRequest] to a [Source] model.
 */
fun InsertionRequest.toSource() = Source(
    id = UUID.random(),
    title = title,
    url = url,
    sourceType = sourceType,
    lastUpdated = Instant.DISTANT_PAST,
)
