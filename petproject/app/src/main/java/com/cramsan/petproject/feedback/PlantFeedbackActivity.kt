package com.cramsan.petproject.feedback

import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.base.NoopViewModel
import com.cramsan.petproject.databinding.ActivityPlantFeedbackBinding

class PlantFeedbackActivity : BaseActivity<NoopViewModel, ActivityPlantFeedbackBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_feedback
    override val titleResource: Int?
        get() = null
    override val enableDataBinding: Boolean
        get() = true
    override val toolbarViewId: Int?
        get() = null
    override val logTag: String
        get() = "PlantFeedbackActivity"
    override val enableUp: Boolean
        get() = false
}
