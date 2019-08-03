package com.cramsan.petproject.plantdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import kotlinx.android.synthetic.main.activity_plant_details.*

class PlantDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreate")
        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(toolbar_2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
