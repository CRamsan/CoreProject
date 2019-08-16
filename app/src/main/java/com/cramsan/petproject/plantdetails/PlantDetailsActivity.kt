package com.cramsan.petproject.plantdetails

import android.os.Bundle
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_plant_details.plant_details_toolbar

class PlantDetailsActivity : BaseActivity(), PlantDetailsFragment.OnDetailsFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(plant_details_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPlantReady(plant: Plant) {
        supportActionBar?.title = plant.mainCommonName
    }

    override fun onPlantMetadataReady(plantMetadata: PlantMetadata) {
        eventLogger.log(Severity.INFO, classTag(), "onPlantMetadataReady called")
    }
}
