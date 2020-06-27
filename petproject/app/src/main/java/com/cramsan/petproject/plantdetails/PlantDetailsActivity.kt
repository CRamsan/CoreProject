package com.cramsan.petproject.plantdetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_plant_details.plant_details_toolbar

class PlantDetailsActivity : BaseActivity<PlantDetailsViewModel>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_plant_details
    override val toolbar: Toolbar?
        get() = plant_details_toolbar
    override val tag: String
        get() = "PlantDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: PlantDetailsViewModel by viewModels()
        model.getPlant().observe(this, Observer {
            if (it == null) {
                eventLogger.log(Severity.WARNING, tag, "Plant is null")
                return@Observer
            }
            supportActionBar?.title = it.mainCommonName
        })
        viewModel = model
    }
}
