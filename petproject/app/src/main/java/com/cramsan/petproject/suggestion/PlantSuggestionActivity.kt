package com.cramsan.petproject.suggestion

import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.base.NoopViewModel
import com.cramsan.petproject.databinding.ActivityPlantSuggestionBinding

class PlantSuggestionActivity : BaseActivity<NoopViewModel, ActivityPlantSuggestionBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_suggestion
    override val logTag: String
        get() = "PlantSuggestionActivity"
    override val enableDataBinding: Boolean
        get() = true
    override val toolbarViewId: Int?
        get() = null
    override val enableUp: Boolean
        get() = false
    override val titleResource: Int?
        get() = null
}
