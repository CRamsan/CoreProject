package com.cramsan.petproject.mainmenu

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentMainMenuBinding
import com.cramsan.petproject.plantslist.PlantsListFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment : BaseFragment<AllPlantListViewModel, FragmentMainMenuBinding>(), AllPlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    override val logTag: String
        get() = "MainMenuFragment"
    override val contentViewLayout: Int
        get() = R.layout.fragment_main_menu

    private lateinit var plantsAdapter: AllPlantsRecyclerViewAdapter
    private lateinit var queryCleaner: OnBackPressedCallback
    private var layoutManager: LinearLayoutManager? = null

    // Only enable the searchView if data is available
    private var enableSearchView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model: AllPlantListViewModel by viewModels()
        viewModel = model
        dataBinding.viewModel = viewModel

        viewModel.observableNextActivityCat().observe(
            viewLifecycleOwner,
            Observer {
                val action = MainMenuFragmentDirections.actionMainMenuFragmentToPlantsListFragment(AnimalType.CAT)
                findNavController().navigate(action)
            }
        )
        viewModel.observableNextActivityDog().observe(
            viewLifecycleOwner,
            Observer {
                val action = MainMenuFragmentDirections.actionMainMenuFragmentToPlantsListFragment(AnimalType.DOG)
                findNavController().navigate(action)
            }
        )
        viewModel.observableShowIsDownloadingData().observe(
            viewLifecycleOwner,
            Observer {
                displayDownloadingMessage()
            }
        )
        viewModel.observableShowDataDownloaded().observe(
            viewLifecycleOwner,
            Observer {
                displayDownloadCompleteMessage()
            }
        )
        viewModel.observablePlants().observe(
            viewLifecycleOwner,
            Observer { plants ->
                plantsAdapter.updateValues(plants)
            }
        )
        viewModel.observableStartDownload().observe(
            viewLifecycleOwner,
            Observer {
                val action = MainMenuFragmentDirections.actionMainMenuFragmentToDownloadCatalogDialogFragment()
                findNavController().navigate(action)
            }
        )
        viewModel.observableShowDataDownloaded().observe(
            viewLifecycleOwner,
            Observer {
                eventLogger.log(Severity.INFO, "MainMenuActivity", "Data is downloaded")
                enableSearchView = true
                requireActivity().invalidateOptionsMenu()
            }
        )
        layoutManager = LinearLayoutManager(context)
        plantsAdapter = AllPlantsRecyclerViewAdapter(this, AnimalType.ALL, eventLogger, requireContext())
        dataBinding.plantListRecycler.layoutManager = layoutManager
        dataBinding.plantListRecycler.adapter = plantsAdapter
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.isCatalogReady()) {
            enableSearchView = true
            requireActivity().invalidateOptionsMenu()
        }
        viewModel.tryStartDownload()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        layoutManager?.let {
            outState.putInt(PlantsListFragment.SCROLL_POS, it.findFirstVisibleItemPosition())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onNewItemSelected(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, "MainMenuFragment", "onNewItemSelected")
        val action = MainMenuFragmentDirections.actionMainMenuFragmentToPlantDetailsFragment(plantId, animalType)
        findNavController().navigate(action)
    }

    private fun displayDownloadingMessage() {
        Snackbar.make(requireView(), R.string.main_menu_snackbar_downloading, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun displayDownloadCompleteMessage() {
        Snackbar.make(requireView(), R.string.main_menu_snackbar_downloaded, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        eventLogger.log(Severity.INFO, "MainMenuActivity", "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = ContextCompat.getSystemService(requireContext(), SearchManager::class.java) ?: return
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

        queryCleaner = requireActivity().onBackPressedDispatcher.addCallback(this) {
            searchView.setQuery("", false)
            queryCleaner.isEnabled = false
            searchView.isIconified = true
        }
        queryCleaner.isEnabled = false

        searchView.apply {
            // Assumes current activity is the searchable activity
            if (viewModel.queryString.value.isNotEmpty()) {
                isIconified = false
                queryCleaner.isEnabled = true
                setQuery(viewModel.queryString.value, false)
            }
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.queryString.value = query
                        queryCleaner.isEnabled = true
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.queryString.value = newText
                        queryCleaner.isEnabled = true
                        return true
                    }
                }
            )
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.children.find { it.itemId == R.id.action_search }!!.isVisible = enableSearchView
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_debug -> {
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToDebugMenuFragment()
            findNavController().navigate(action)
            true
        }
        R.id.action_search -> {
            super.onOptionsItemSelected(item)
        }
        R.id.action_about -> {
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToAboutFragment()
            findNavController().navigate(action)
            true
        }
        else -> {
            eventLogger.log(Severity.DEBUG, "MainMenuActivity", "Action item not handled")
            super.onOptionsItemSelected(item)
        }
    }
}
