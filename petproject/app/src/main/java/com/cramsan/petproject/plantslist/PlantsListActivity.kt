package com.cramsan.petproject.plantslist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.databinding.ActivityPlantsListBinding

class PlantsListActivity : BaseActivity<PlantListViewModel, ActivityPlantsListBinding>() {

    override val contentViewLayout: Int
        get() = R.layout.activity_plants_list
    override val titleResource: Int?
        get() = null
    override val toolbarViewId: Int?
        get() = R.id.plant_list_toolbar
    override val enableDataBinding: Boolean
        get() = true
    override val enableUp: Boolean
        get() = true
    override val logTag: String
        get() = "PlantsListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: PlantListViewModel by viewModels()
        model.observableAnimalType().observe(this, Observer {
            when (it) {
                AnimalType.CAT -> supportActionBar?.setTitle(R.string.title_fragment_plants_cats)
                AnimalType.DOG -> supportActionBar?.setTitle(R.string.title_fragment_plants_dogs)
                else -> TODO()
            }
        })
        model.queryString = ""

        viewModel = model
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_QUERY, viewModel.queryString)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_QUERY)?.let {
            viewModel.queryString = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        eventLogger.log(Severity.INFO, "PlantsListActivity", "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.plant_list, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.queryString = query
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.queryString = newText
                    return true
                }
            })
        }

        return true
    }

    companion object {
        const val SEARCH_QUERY = "searchQuery"
    }
}
