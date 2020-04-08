package com.cramsan.petproject.appcore.feedback.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO
import com.cramsan.petproject.appcore.feedback.FeedbackManagerInterface
import com.cramsan.petproject.appcore.model.feedback.Feedback

class FeedbackManager(
    private val platformDelegate: FeedbackManagerDAO,
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface
) : FeedbackManagerInterface {

    override fun submitFeedback(feedback: Feedback) {
        platformDelegate.submitFeedback(feedback)
    }
}
