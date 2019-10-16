package com.cramsan.petproject.edit

import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity

class PlantEditActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_edit
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = null
}
