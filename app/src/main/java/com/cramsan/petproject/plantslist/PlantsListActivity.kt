package com.cramsan.petproject.plantslist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsActivity.Companion.PLANT_ID

import kotlinx.android.synthetic.main.activity_plants_list.*

class PlantsListActivity : AppCompatActivity(), PlantsListFragment.OnListFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plants_list)
        setSupportActionBar(toolbar_2)
    }

    override fun onListFragmentInteraction(item: Plant) {
        val plantIntent = Intent(this, PlantDetailsActivity::class.java)
        plantIntent.putExtra(PLANT_ID, item.id)
        startActivity(plantIntent)
    }
}
