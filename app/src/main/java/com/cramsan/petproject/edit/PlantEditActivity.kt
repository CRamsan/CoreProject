package com.cramsan.petproject.edit

import androidx.appcompat.widget.Toolbar
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment
import kotlinx.android.synthetic.main.activity_plant_edit.plant_edit_toolbar

class PlantEditActivity : BaseActivity(), PlantEditFragment.OnEditFragmentInteractionListener {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_edit
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = plant_edit_toolbar

    override fun onPlantReady(plant: Plant) {
        supportActionBar?.title = plant.mainCommonName
    }

    override fun onPlantMetadataReady(plantMetadata: PlantMetadata) {
        eventLogger.log(Severity.INFO, classTag(), "onPlantMetadataReady called")
    }
}
