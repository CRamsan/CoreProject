package com.cramsan.petproject.plantdetails

import androidx.appcompat.widget.Toolbar
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_plant_details.plant_details_toolbar

class PlantDetailsActivity : BaseActivity(), PlantDetailsFragment.OnDetailsFragmentInteractionListener {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_details
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = plant_details_toolbar
    override val tag: String
        get() = "PlantDetailsActivity"

    override fun onPlantReady(plant: Plant) {
        supportActionBar?.title = plant.mainCommonName
    }

    override fun onPlantMetadataReady(plantMetadata: PlantMetadata) {
        eventLogger.log(Severity.INFO, "PlantDetailsActivity", "onPlantMetadataReady called")
    }
}
