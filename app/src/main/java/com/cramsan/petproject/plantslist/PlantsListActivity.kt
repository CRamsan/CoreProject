package com.cramsan.petproject.plantslist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsActivity.Companion.PLANT_ID

import kotlinx.android.synthetic.main.activity_plants_list.*
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI

class PlantsListActivity : AppCompatActivity(), PlantsListFragment.OnListFragmentInteractionListener {

    private var queryTextListener: OnQueryTextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreate")
        setContentView(R.layout.activity_plants_list)
        setSupportActionBar(toolbar_2)
    }

    override fun onListFragmentInteraction(plantId: Int, animalType: AnimalType) {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onListFragmentInteraction")
        val plantIntent = Intent(this, PlantDetailsActivity::class.java)
        plantIntent.putExtra(PLANT_ID, plantId)
        startActivity(plantIntent)
    }

    override fun onRegisterAsSearchable(listener: OnQueryTextListener) {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onRegisterAsSearchable")
        queryTextListener = listener
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreateOptionsMenu")

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_plants_list, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    queryTextListener?.onQueryTextSubmit(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    queryTextListener?.onQueryTextChange(newText)
                    return true
                }
            })

        }
        return true
    }
}
