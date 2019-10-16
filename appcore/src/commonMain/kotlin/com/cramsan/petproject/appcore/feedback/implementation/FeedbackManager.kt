package com.cramsan.petproject.appcore.feedback.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO
import com.cramsan.petproject.appcore.feedback.FeedbackManagerInterface
import com.cramsan.petproject.appcore.model.feedback.Feedback

class FeedbackManager(
    initializer: FeedbackManagerInitializer,
    private val eventLogger: EventLoggerInterface,
    private val threadUtil: ThreadUtilInterface
) : FeedbackManagerInterface {

    private var feedbackManagerDAO: FeedbackManagerDAO = initializer.platformInitializer.getFeedbackManagerDAO()

    override fun submitFeedback(feedback: Feedback) {
        feedbackManagerDAO.submitFeedback(feedback)
    }
}
