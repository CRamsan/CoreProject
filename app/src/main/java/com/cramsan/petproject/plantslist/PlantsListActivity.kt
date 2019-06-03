package com.cramsan.petproject.plantslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.cramsan.petproject.R

import kotlinx.android.synthetic.main.activity_plants_list.*

class PlantsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plants_list)
        setSupportActionBar(toolbar_2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
