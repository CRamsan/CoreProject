package com.cramsan.petproject.appcore.feedback.implementation

import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO

actual class FeedbackManagerPlatformInitializer(private val feedbackManagerDAOImpl: FeedbackManagerDAO) {
    actual fun getFeedbackManagerDAO(): FeedbackManagerDAO = feedbackManagerDAOImpl
}
