package com.cramsan.petproject.mainmenu

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.about.AboutActivity
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.debugmenu.DebugMenuActivity
import kotlinx.android.synthetic.main.activity_main_menu.main_menu_toolbar

class MainMenuActivity : BaseActivity<AllPlantListViewModel>() {

    override val contentViewLayout: Int
        get() = R.layout.activity_main_menu
    override val titleResource: Int?
        get() = R.string.app_name
    override val toolbar: Toolbar?
        get() = main_menu_toolbar
    override val enableUp: Boolean
        get() = false
    override val tag: String
        get() = "MainMenuActivity"

    private var enableSearchView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: AllPlantListViewModel by viewModels()
        model.observableIsCatalogReady().observe(this, Observer { isReady ->
            eventLogger.log(Severity.INFO, "MainMenuActivity", "observableDownloadingChanged: $isReady")
            enableSearchView = isReady
            invalidateOptionsMenu()
        })
        viewModel = model
    }

    override fun onStart() {
        super.onStart()
        if (viewModel?.isCatalogReady() == true) {
            enableSearchView = true
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        eventLogger.log(Severity.INFO, "MainMenuActivity", "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel?.searchPlants(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel?.searchPlants(newText)
                    return true
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_debug -> {
            val intent = Intent(this, DebugMenuActivity::class.java)
            startActivity(intent)
            true
        }

        R.id.action_search -> {
            super.onOptionsItemSelected(item)
        }
        R.id.action_about -> {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            eventLogger.log(Severity.DEBUG, "MainMenuActivity", "Action item not handled")
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchView = menu.findItem(R.id.action_search) as MenuItem
        searchView.isVisible = enableSearchView
        return true
    }
}
