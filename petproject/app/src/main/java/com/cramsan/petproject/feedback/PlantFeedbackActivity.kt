package com.cramsan.petproject.feedback

import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity

class PlantFeedbackActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_feedback
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = null
    override val tag: String
        get() = "PlantFeedbackActivity"
}
