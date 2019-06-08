package com.cramsan.petproject.plantdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cramsan.petproject.R
import kotlinx.android.synthetic.main.activity_plant_details.*

class PlantDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(toolbar_2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        val PLANT_ID = "plantId"
    }
}
