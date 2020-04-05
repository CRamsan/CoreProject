package com.cramsan.petproject.mainmenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.download.DownloadDialogActivity
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment
import com.cramsan.petproject.plantslist.PlantsListActivity
import com.cramsan.petproject.plantslist.PlantsListFragment
import com.cramsan.petproject.plantslist.PlantsListFragment.Companion.ANIMAL_TYPE
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_cats
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_dogs
import kotlinx.android.synthetic.main.fragment_main_menu.plant_list_recycler
import kotlinx.android.synthetic.main.fragment_main_menu.plant_main_menu_list_view
import kotlinx.android.synthetic.main.fragment_main_menu.plant_main_menu_view
import kotlinx.android.synthetic.main.fragment_main_menu.plants_list_loading

class MainMenuFragment : BaseFragment(), SearchView.OnQueryTextListener,
AllPlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    override val contentViewLayout: Int
        get() = R.layout.fragment_new_main_menu

    private var listener: PlantsListFragment.OnListFragmentInteractionListener? = null
    private lateinit var viewModel: DownloadCatalogViewModel
    private lateinit var plantsAdapter: AllPlantsRecyclerViewAdapter
    private lateinit var model: AllPlantListViewModel
    private val animalType = AnimalType.ALL
    private lateinit var layoutManager: LinearLayoutManager
    private var searchQuery: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PlantsListFragment.OnListFragmentInteractionListener) {
            listener = context
            context.onNewSearchable(this)
        } else {
            throw PlantsListFragment.InvalidContextException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = getViewModel(DownloadCatalogViewModel::class.java)
        main_menu_cats.setOnClickListener {
            val intent = Intent(requireContext(), PlantsListActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, AnimalType.CAT.ordinal)
            startActivity(intent)
        }
        main_menu_dogs.setOnClickListener {
            val intent = Intent(requireContext(), PlantsListActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, AnimalType.DOG.ordinal)
            startActivity(intent)
        }

        var startingOffset: Int? = null
        if (savedInstanceState != null) {
            startingOffset = savedInstanceState.getInt(PlantsListFragment.SCROLL_POS, 0)
            searchQuery = savedInstanceState.getString(PlantsListFragment.SEARCH_QUERY)
        }

        listener?.onAnimalTypeReady(AnimalType.ALL)

        layoutManager = LinearLayoutManager(context)
        plantsAdapter = AllPlantsRecyclerViewAdapter(this, animalType, requireContext())
        plant_list_recycler.layoutManager = layoutManager
        plant_list_recycler.adapter = plantsAdapter

        model = getViewModel(AllPlantListViewModel::class.java)
        model.animalType = animalType
        model.observablePlants().observe(viewLifecycleOwner, Observer<List<PresentablePlant>> { plants ->
            plantsAdapter.updateValues(plants)
        })
        model.observableLoading().observe(viewLifecycleOwner, Observer<Boolean> { isLoading ->
            if (isLoading) {
                plants_list_loading.visibility = View.VISIBLE
                plant_list_recycler.visibility = View.GONE
            } else {
                plants_list_loading.visibility = View.GONE
                plant_list_recycler.visibility = View.VISIBLE
            }
        })
        startingOffset?.let {
            layoutManager.scrollToPosition(startingOffset)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.isCatalogReady()) {
            val intent = Intent(requireContext(), DownloadDialogActivity::class.java)
            startActivity(intent)
        }
        val loadedSearchQuery = searchQuery
        if (loadedSearchQuery?.isNotBlank() == true) {
            plant_main_menu_view.visibility = View.GONE
            plant_main_menu_list_view.visibility = View.VISIBLE
            model.searchPlants(loadedSearchQuery)
        } else {
            model.reloadPlants()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        outState.putInt(PlantsListFragment.SCROLL_POS, layoutManager.findFirstVisibleItemPosition())
        outState.putString(PlantsListFragment.SEARCH_QUERY, searchQuery)

        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onNewItemSelected(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, "MainMenuFragment", "onNewItemSelected")
        val plantIntent = Intent(requireContext(), PlantDetailsActivity::class.java)
        plantIntent.putExtra(PlantDetailsFragment.PLANT_ID, plantId)
        plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
        startActivity(plantIntent)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        eventLogger.log(Severity.DEBUG, "MainMenuFragment", "onQueryTextSubmit")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        eventLogger.log(Severity.DEBUG, "MainMenuFragment", "onQueryTextChange")
        newText?.let {
            if (!newText.isEmpty()) {
                searchQuery = newText
                plant_main_menu_view.visibility = View.GONE
                plant_main_menu_list_view.visibility = View.VISIBLE
            } else {
                plant_main_menu_view.visibility = View.VISIBLE
                plant_main_menu_list_view.visibility = View.GONE
            }
            model.searchPlants(it)
        }
        return true
    }
}
