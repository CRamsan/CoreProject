package com.cramsan.petproject

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.transition.Fade
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.about.AboutActivity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment.Companion.PLANT_ID
import com.cramsan.petproject.plantslist.PlantsListFragment
import com.cramsan.petproject.plantslist.PlantsListFragment.Companion.ANIMAL_TYPE
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.nav_view

class MainActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    PlantsListFragment.OnListFragmentInteractionListener {

    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private var selectedTabId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
            val toolbar: Toolbar = findViewById(R.id.main_toolbar)
            setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
            val toggle = ActionBarDrawerToggle(
                        this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        var tabToLoad = R.id.nav_list_cat
        val selectedMenuItem: MenuItem?
        if (savedInstanceState != null) {
            tabToLoad = savedInstanceState.getInt(SELECTED_TAB, -1)
            if (tabToLoad == -1) {
                selectedMenuItem = nav_view.menu.findItem(tabToLoad)
                onNavigationItemSelected(selectedMenuItem)
            } else {
                selectedMenuItem = nav_view.menu.findItem(tabToLoad)
                selectedTabId = tabToLoad
                when (selectedMenuItem.itemId) {
                    R.id.nav_list_cat -> {
                        supportActionBar?.setTitle(R.string.title_fragment_plants_cats)
                    }
                    R.id.nav_list_dog -> {
                        supportActionBar?.setTitle(R.string.title_fragment_plants_dogs)
                    }
                }
            }
        } else {
            selectedMenuItem = nav_view.menu.findItem(tabToLoad)
            onNavigationItemSelected(selectedMenuItem)
        }
        nav_view.setCheckedItem(selectedMenuItem)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        // Save the user's current game state
        outState?.run {
            putInt(SELECTED_TAB, selectedTabId)
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        eventLogger.log(Severity.INFO, classTag(), "onBackPressed")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

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

    private fun setFragmentForAnimalType(animalType: AnimalType) {
        val newFragment = PlantsListFragment.newInstance(animalType)
        val transaction = supportFragmentManager.beginTransaction()

        val previousFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        val exitFade = Fade()
        exitFade.duration = FADE_DURATION
        previousFragment?.exitTransition = exitFade

        val enterFade = Fade()
        enterFade.startDelay = FADE_DURATION
        enterFade.duration = FADE_DURATION
        newFragment.enterTransition = enterFade

        transaction.replace(R.id.main_container, newFragment)
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        eventLogger.log(Severity.INFO, classTag(), "onOptionsItemSelected with index ${item.order}")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        if (selectedTabId == item.itemId) {
            eventLogger.log(Severity.DEBUG, classTag(), "Tapping on previously selected menu item")
            return true
        }

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_list_cat -> {
                supportActionBar?.setTitle(R.string.title_fragment_plants_cats)
                setFragmentForAnimalType(AnimalType.CAT)
                selectedTabId = item.itemId
            }
            R.id.nav_list_dog -> {
                supportActionBar?.setTitle(R.string.title_fragment_plants_dogs)
                setFragmentForAnimalType(AnimalType.DOG)
                selectedTabId = item.itemId
            }
            R.id.nav_about -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return true
    }

    override fun onListFragmentInteraction(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, classTag(), "onListFragmentInteraction")
        val plantIntent = Intent(this, PlantDetailsActivity::class.java)
        plantIntent.putExtra(PLANT_ID, plantId)
        plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
        startActivity(plantIntent)
    }

    override fun onRegisterAsSearchable(listener: SearchView.OnQueryTextListener) {
        eventLogger.log(Severity.INFO, classTag(), "onRegisterAsSearchable")
        queryTextListener = listener
    }

    companion object {
        const val SELECTED_TAB = "selectedTab"
        const val FADE_DURATION = 100L
    }
}
