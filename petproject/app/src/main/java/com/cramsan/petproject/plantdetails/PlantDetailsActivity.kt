package com.cramsan.petproject.plantdetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.databinding.ActivityPlantDetailsBinding

class PlantDetailsActivity : BaseActivity<PlantDetailsViewModel, ActivityPlantDetailsBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_details
    override val toolbarViewId: Int?
        get() = R.id.plant_details_toolbar
    override val enableUp: Boolean
        get() = true
    override val enableDataBinding: Boolean
        get() = true
    override val titleResource: Int?
        get() = null
    override val logTag: String
        get() = "PlantDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: PlantDetailsViewModel by viewModels()
        model.observablePlantName.observe(
            this,
            Observer {
                supportActionBar?.title = it
            }
        )
        viewModel = model
    }
}
