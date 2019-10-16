package com.cramsan.petproject.appcore.feedback

import com.cramsan.petproject.appcore.model.feedback.Feedback

interface FeedbackManagerDAO {
    fun submitFeedback(feedback: Feedback)
}
