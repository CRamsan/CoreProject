package com.cramsan.petproject

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment.Companion.PLANT_ID
import com.cramsan.petproject.plantslist.PlantsListFragment
import com.cramsan.petproject.plantslist.PlantsListFragment.Companion.ANIMAL_TYPE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    PlantsListFragment.OnListFragmentInteractionListener{

    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private var selectedTabId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreate")

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        var tabToLoad = R.id.nav_home
        val selectedMenuItem: MenuItem?
        if (savedInstanceState != null) {
            tabToLoad = savedInstanceState.getInt(SELECTED_TAB, -1)
            if (tabToLoad == -1) {
                selectedMenuItem = nav_view.menu.findItem(tabToLoad)
                onNavigationItemSelected(selectedMenuItem)
            } else {
                selectedMenuItem = nav_view.menu.findItem(tabToLoad)
                selectedTabId = tabToLoad
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
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onBackPressed")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun setFragmentForAnimalType(animalType: AnimalType) {
        val newFragment = PlantsListFragment.newInstance(animalType)
        val transaction = supportFragmentManager.beginTransaction()

        val previousFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        val exitFade = Fade()
        exitFade.duration = 100
        previousFragment?.exitTransition = exitFade

        val enterFade = Fade()
        enterFade.startDelay = 100
        enterFade.duration = 100
        newFragment.enterTransition = enterFade

        transaction.replace(R.id.main_container, newFragment)
        transaction.commit()
        when(animalType) {
            AnimalType.CAT -> supportActionBar?.setTitle(R.string.title_fragment_plants_cats)
            AnimalType.DOG -> supportActionBar?.setTitle(R.string.title_fragment_plants_dogs)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onOptionsItemSelected with index ${item.order}")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        if (selectedTabId == item.itemId) {
            CoreFrameworkAPI.eventLogger.log(Severity.DEBUG, classTag(), "Tapping on previously selected menu item")
            return true
        }

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                setFragmentForAnimalType(AnimalType.CAT)
            }
            R.id.nav_gallery -> {
                setFragmentForAnimalType(AnimalType.DOG)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        selectedTabId = item.itemId
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onListFragmentInteraction(plantId: Int, animalType: AnimalType) {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onListFragmentInteraction")
        val plantIntent = Intent(this, PlantDetailsActivity::class.java)
        plantIntent.putExtra(PLANT_ID, plantId)
        plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
        startActivity(plantIntent)
    }

    override fun onRegisterAsSearchable(listener: SearchView.OnQueryTextListener) {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onRegisterAsSearchable")
        queryTextListener = listener
    }

    companion object {
        const val SELECTED_TAB = "selectedTab"
    }
}
