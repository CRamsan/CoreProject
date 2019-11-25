package com.cramsan.petproject.appcore.feedback.implementation

import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO

expect class FeedbackManagerPlatformInitializer {
    fun getFeedbackManagerDAO(): FeedbackManagerDAO
}
