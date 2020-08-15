package com.cramsan.petproject.mainmenu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentMainMenuBinding
import com.cramsan.petproject.download.DownloadCatalogDialogActivity
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment
import com.cramsan.petproject.plantslist.PlantsListActivity
import com.cramsan.petproject.plantslist.PlantsListFragment
import com.cramsan.petproject.plantslist.PlantsListFragment.Companion.ANIMAL_TYPE
import com.google.android.material.snackbar.Snackbar

class MainMenuFragment : BaseFragment<AllPlantListViewModel, FragmentMainMenuBinding>(), AllPlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    override val logTag: String
        get() = "MainMenuFragment"
    override val contentViewLayout: Int
        get() = R.layout.fragment_main_menu

    private lateinit var plantsAdapter: AllPlantsRecyclerViewAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model: AllPlantListViewModel by activityViewModels()
        viewModel = model
        dataBinding.viewModel = viewModel

        viewModel.observableNextActivityCat().observe(
            requireActivity(),
            Observer {
                val intent = Intent(requireContext(), PlantsListActivity::class.java)
                intent.putExtra(ANIMAL_TYPE, AnimalType.CAT.ordinal)
                startActivity(intent)
            }
        )
        viewModel.observableNextActivityDog().observe(
            requireActivity(),
            Observer {
                val intent = Intent(requireContext(), PlantsListActivity::class.java)
                intent.putExtra(ANIMAL_TYPE, AnimalType.DOG.ordinal)
                startActivity(intent)
            }
        )
        viewModel.observableShowIsDownloadingData().observe(
            requireActivity(),
            Observer {
                displayDownloadingMessage()
            }
        )
        viewModel.observableShowDataDownloaded().observe(
            requireActivity(),
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
                val intent = Intent(requireContext(), DownloadCatalogDialogActivity::class.java)
                startActivity(intent)
            }
        )

        layoutManager = LinearLayoutManager(context)
        plantsAdapter = AllPlantsRecyclerViewAdapter(this, AnimalType.ALL, requireContext())
        dataBinding.plantListRecycler.layoutManager = layoutManager
        dataBinding.plantListRecycler.adapter = plantsAdapter
    }

    override fun onStart() {
        super.onStart()
        viewModel.tryStartDownload()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PlantsListFragment.SCROLL_POS, layoutManager.findFirstVisibleItemPosition())
        super.onSaveInstanceState(outState)
    }

    override fun onNewItemSelected(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, "MainMenuFragment", "onNewItemSelected")
        val plantIntent = Intent(requireContext(), PlantDetailsActivity::class.java)
        plantIntent.putExtra(PlantDetailsFragment.PLANT_ID, plantId)
        plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
        startActivity(plantIntent)
    }

    private fun displayDownloadingMessage() {
        Snackbar.make(requireView(), R.string.main_menu_snackbar_downloading, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun displayDownloadCompleteMessage() {
        Snackbar.make(requireView(), R.string.main_menu_snackbar_downloaded, Snackbar.LENGTH_SHORT)
            .show()
    }
}
