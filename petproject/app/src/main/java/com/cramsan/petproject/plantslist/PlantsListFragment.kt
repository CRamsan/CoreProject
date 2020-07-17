package com.cramsan.petproject.plantslist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentPlantsListBinding
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment.Companion.PLANT_ID
import com.cramsan.petproject.suggestion.PlantSuggestionActivity

/**
 * A fragment representing a list of Items.
 */
class PlantsListFragment : BaseFragment<PlantListViewModel, FragmentPlantsListBinding>(), PlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    override val contentViewLayout: Int
        get() = R.layout.fragment_plants_list
    override val logTag: String
        get() = "PlantsListFragment"

    private lateinit var plantsAdapter: PlantsRecyclerViewAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var animalType: AnimalType
    private var searchQuery: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var startingOffset: Int? = null
        val model: PlantListViewModel by activityViewModels()

        if (savedInstanceState != null) {
            startingOffset = savedInstanceState.getInt(SCROLL_POS, 0)
            searchQuery = savedInstanceState.getString(SEARCH_QUERY)
            val animalTypeId = savedInstanceState.getInt(ANIMAL_TYPE, -1)
            animalType = AnimalType.values()[animalTypeId]
        } else {
            val animalTypeId = requireActivity().intent?.getIntExtra(ANIMAL_TYPE, -1)
            animalType = if (animalTypeId == null || animalTypeId == -1) {
                model.observableAnimalType().value!!
            } else {
                AnimalType.values()[animalTypeId]
            }
        }
        model.setAnimalType(animalType)

        layoutManager = LinearLayoutManager(context)
        plantsAdapter = PlantsRecyclerViewAdapter(this, animalType, requireContext())
        dataBinding.plantListRecycler.layoutManager = layoutManager
        dataBinding.plantListRecycler.adapter = plantsAdapter

        model.observablePlants().observe(viewLifecycleOwner, Observer<List<PresentablePlant>> { plants ->
            plantsAdapter.updateValues(plants)
        })
        model.observableLoading().observe(viewLifecycleOwner, Observer<Boolean> { isLoading ->
            if (isLoading) {
                dataBinding.plantsListLoading.visibility = View.VISIBLE
                dataBinding.plantListRecycler.visibility = View.GONE
            } else {
                dataBinding.plantsListLoading.visibility = View.GONE
                dataBinding.plantListRecycler.visibility = View.VISIBLE
            }
        })
        dataBinding.plantListAddPlant.setOnClickListener {
            val intent = Intent(requireContext(), PlantSuggestionActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, animalType.ordinal)
            startActivity(intent)
        }
        viewModel = model

        startingOffset?.let {
            layoutManager.scrollToPosition(startingOffset)
        }
    }

    override fun onStart() {
        super.onStart()
        val loadedSearchQuery = searchQuery
        if (loadedSearchQuery?.isNotBlank() == true) {
            viewModel?.searchPlants(loadedSearchQuery)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        outState.putInt(SCROLL_POS, layoutManager.findFirstVisibleItemPosition())
        outState.putString(SEARCH_QUERY, searchQuery)

        super.onSaveInstanceState(outState)
    }

    override fun onNewItemSelected(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, "PlantsListFragment", "onNewItemSelected")
        val plantIntent = Intent(requireContext(), PlantDetailsActivity::class.java)
        plantIntent.putExtra(PLANT_ID, plantId)
        plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
        startActivity(plantIntent)
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val SEARCH_QUERY = "searchQuery"
        const val SCROLL_POS = "scrollPosition"
    }
}
