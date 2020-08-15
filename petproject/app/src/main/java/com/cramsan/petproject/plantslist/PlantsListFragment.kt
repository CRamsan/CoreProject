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
import com.cramsan.petproject.download.DownloadCatalogDialogActivity
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var startingOffset: Int? = null
        val model: PlantListViewModel by activityViewModels()
        viewModel = model

        if (savedInstanceState != null) {
            startingOffset = savedInstanceState.getInt(SCROLL_POS, 0)
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
        dataBinding.viewModel = model

        model.observablePlants().observe(
            viewLifecycleOwner,
            Observer<List<PresentablePlant>> { plants ->
                plantsAdapter.updateValues(plants)
            }
        )
        viewModel.observableStartDownload().observe(
            viewLifecycleOwner,
            Observer {
                val intent = Intent(requireContext(), DownloadCatalogDialogActivity::class.java)
                startActivity(intent)
            }
        )
        viewModel.observableShowIsDownloadingData().observe(
            requireActivity(),
            Observer {
                // TODO: Implement some UI to let the user know that data is being downloaded
            }
        )
        viewModel.observableShowDataDownloaded().observe(
            requireActivity(),
            Observer {
                // TODO: Implement some UI to let the user know that the data was downloaded
            }
        )
        viewModel.observableDownloadingVisibility().observe(
            requireActivity(),
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
            val intent = Intent(requireContext(), PlantSuggestionActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, animalType.ordinal)
            startActivity(intent)
        }

        startingOffset?.let {
            layoutManager.scrollToPosition(startingOffset)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        if (viewModel.queryString.isNotBlank()) {
            outState.putInt(SCROLL_POS, layoutManager.findFirstVisibleItemPosition())
        }

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
        const val SCROLL_POS = "scrollPosition"
    }
}
