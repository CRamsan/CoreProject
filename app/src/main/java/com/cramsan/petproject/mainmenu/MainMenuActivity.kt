package com.cramsan.petproject.mainmenu

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
import com.cramsan.petproject.plantslist.PlantsListFragment
import kotlinx.android.synthetic.main.activity_main_menu.main_menu_toolbar

class MainMenuActivity : BaseActivity(), PlantsListFragment.OnListFragmentInteractionListener {

    override val contentViewLayout: Int
        get() = R.layout.activity_main_menu
    override val titleResource: Int?
        get() = R.string.app_name
    override val toolbar: Toolbar?
        get() = main_menu_toolbar
    override val enableUp: Boolean
        get() = false

    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        eventLogger.log(Severity.INFO, classTag(), "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

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

    override fun onNewSearchable(listener: SearchView.OnQueryTextListener) {
        eventLogger.log(Severity.INFO, classTag(), "onNewSearchable")
        queryTextListener = listener
    }

    override fun onAnimalTypeReady(animalType: AnimalType) {
    }

}
