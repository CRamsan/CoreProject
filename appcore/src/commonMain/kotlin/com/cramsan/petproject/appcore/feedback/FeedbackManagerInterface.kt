package com.cramsan.petproject.appcore.feedback

import com.cramsan.petproject.appcore.model.feedback.Feedback

interface FeedbackManagerInterface {
    fun submitFeedback(feedback: Feedback)
}
