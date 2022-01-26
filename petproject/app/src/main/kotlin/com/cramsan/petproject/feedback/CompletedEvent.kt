package com.cramsan.petproject.feedback

import com.cramsan.framework.core.BaseEvent

/**
 * Event class that represents a feedback item was submitted.
 */
class CompletedEvent(
    /**
     * [true] If the feedback was submitted succesfully, [false] otherwise.
     */
    val feedbackSubmitted: Boolean
) : BaseEvent()
