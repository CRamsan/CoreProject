package com.cramsan.petproject.plantdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import kotlinx.android.synthetic.main.activity_plant_details.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantDetailsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val eventLogger: EventLoggerInterface by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, classTag(), "onCreate")
        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(toolbar_2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
