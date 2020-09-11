package com.cramsan.petproject.plantslist

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentPlantsListBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class PlantsListFragment : BaseFragment<PlantListViewModel, FragmentPlantsListBinding>(), PlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    override val contentViewLayout: Int
        get() = R.layout.fragment_plants_list
    override val logTag: String
        get() = "PlantsListFragment"
    val args: PlantsListFragmentArgs by navArgs()

    private var plantsAdapter: PlantsRecyclerViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var animalType: AnimalType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val animalTypeArg = if (savedInstanceState != null) {
            val animalTypeId = savedInstanceState.getInt(ANIMAL_TYPE, -1)
            AnimalType.values()[animalTypeId]
        } else {
            args.AnimalType
        }
        animalType = animalTypeArg
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var startingOffset: Int? = null
        val model: PlantListViewModel by viewModels()
        viewModel = model
        viewModel.setAnimalType(animalType)

        if (savedInstanceState != null) {
            startingOffset = savedInstanceState.getInt(SCROLL_POS, 0)
        }

        val activity = requireActivity() as AppCompatActivity
        when (animalType) {
            AnimalType.CAT -> activity.supportActionBar?.setTitle(R.string.title_fragment_plants_cats)
            AnimalType.DOG -> activity.supportActionBar?.setTitle(R.string.title_fragment_plants_dogs)
            else -> TODO()
        }

        val linearLayoutManager = LinearLayoutManager(context)
        layoutManager = linearLayoutManager
        val plantsRecyclerAdapter = PlantsRecyclerViewAdapter(this, animalType, eventLogger, requireContext())
        plantsAdapter = plantsRecyclerAdapter
        dataBinding.plantListRecycler.layoutManager = layoutManager
        dataBinding.plantListRecycler.adapter = plantsRecyclerAdapter
        dataBinding.viewModel = model

        model.observablePlants().observe(
            viewLifecycleOwner,
            Observer<List<PresentablePlant>> { plants ->
                plantsRecyclerAdapter.updateValues(plants)
            }
        )
        viewModel.observableStartDownload().observe(
            viewLifecycleOwner,
            Observer {
                val action = PlantsListFragmentDirections.actionPlantsListFragmentToDownloadCatalogDialogFragment()
                findNavController().navigate(action)
            }
        )
        viewModel.observableShowIsDownloadingData().observe(
            viewLifecycleOwner,
            Observer {
                // TODO: Implement some UI to let the user know that data is being downloaded
            }
        )
        viewModel.observableShowDataDownloaded().observe(
            viewLifecycleOwner,
            Observer {
                // TODO: Implement some UI to let the user know that the data was downloaded
            }
        )
        viewModel.observableDownloadingVisibility().observe(
            viewLifecycleOwner,
            Observer {
                // TODO: Update visibility to use boolean states instead of ints.
                if (it == View.VISIBLE) {
                    viewModel.setLoadingMode(true)
                } else {
                    viewModel.setLoadingMode(false)
                }
            }
        )

        dataBinding.plantListAddPlant.setOnClickListener {
            val action = PlantsListFragmentDirections.actionPlantsListFragmentToPlantSuggestionFragment()
            findNavController().navigate(action)
        }

        startingOffset?.let {
            linearLayoutManager.scrollToPosition(startingOffset)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        layoutManager?.let {
            if (viewModel.queryString.isNotBlank()) {
                outState.putInt(SCROLL_POS, it.findFirstVisibleItemPosition())
            }
        }

        super.onSaveInstanceState(outState)
    }

    override fun onNewItemSelected(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, "PlantsListFragment", "onNewItemSelected")
        val action = PlantsListFragmentDirections.actionPlantsListFragmentToPlantDetailsFragment(plantId, animalType)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        eventLogger.log(Severity.INFO, "PlantsListActivity", "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.plant_list, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = ContextCompat.getSystemService(requireContext(), SearchManager::class.java) ?: return
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.queryString = query
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.queryString = newText
                        return true
                    }
                }
            )
        }
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val SCROLL_POS = "scrollPosition"
    }
}