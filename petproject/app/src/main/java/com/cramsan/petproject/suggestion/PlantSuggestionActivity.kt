package com.cramsan.petproject.suggestion

import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity

class PlantSuggestionActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_suggestion
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = null
    override val tag: String
        get() = "PlantSuggestionActivity"
}
