package com.cramsan.petproject.appcore.model.feedback

data class Feedback(
    val id: Long,
    val type: FeedbackType,
    val suggestion: String,
    val referenceId: Long
)

enum class FeedbackType {
    NEW_PLANT,
    NEW_COMMON_NAME,
    WRONG_NAME,
    WRONG_SCIENTIFIC_NAME,
    WRONG_COMMON_NAME,
    WRONG_FAMILY,
    WRONG_TOXICITY
}
