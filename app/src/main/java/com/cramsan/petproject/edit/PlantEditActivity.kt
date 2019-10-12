package com.cramsan.petproject.edit

import androidx.appcompat.widget.Toolbar
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_plant_edit.plant_edit_toolbar

class PlantEditActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_edit
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = plant_edit_toolbar
}
