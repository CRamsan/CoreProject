package com.cramsan.petproject.suggestion

import com.cramsan.framework.core.BaseEvent

/**
 * Event that represents that the operation to submit a suggestion was completed.
 */
class CompletedEvent(
    /**
     * [true] if the suggestion was submitted succesfully, [false] otherwise.
     */
    val suggestionSubmitted: Boolean
) : BaseEvent()
