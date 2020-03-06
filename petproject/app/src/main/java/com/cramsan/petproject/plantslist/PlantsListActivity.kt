package com.cramsan.petproject.plantslist

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_plants_list.plant_list_toolbar

class PlantsListActivity : BaseActivity(), PlantsListFragment.OnListFragmentInteractionListener {
    override val contentViewLayout: Int
        get() = R.layout.activity_plants_list
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = plant_list_toolbar

    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onNewSearchable(listener: SearchView.OnQueryTextListener) {
        eventLogger.log(Severity.INFO, classTag(), "onNewSearchable")
        queryTextListener = listener
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        eventLogger.log(Severity.INFO, classTag(), "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.plant_list, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    queryTextListener?.onQueryTextChange(query)
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

    override fun onAnimalTypeReady(animalType: AnimalType) {
        when (animalType) {
            AnimalType.CAT -> supportActionBar?.setTitle(R.string.title_fragment_plants_cats)
            AnimalType.DOG -> supportActionBar?.setTitle(R.string.title_fragment_plants_dogs)
            else -> TODO()
        }
    }
}
