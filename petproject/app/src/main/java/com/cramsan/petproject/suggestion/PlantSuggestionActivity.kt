package com.cramsan.petproject.suggestion

import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.base.NoopViewModel

class PlantSuggestionActivity : BaseActivity<NoopViewModel>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_suggestion
    override val tag: String
        get() = "PlantSuggestionActivity"
}
