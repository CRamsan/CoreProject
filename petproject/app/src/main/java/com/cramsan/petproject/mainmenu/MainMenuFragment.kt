package com.cramsan.petproject.mainmenu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.download.DownloadDialogActivity
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment
import com.cramsan.petproject.plantslist.PlantsListActivity
import com.cramsan.petproject.plantslist.PlantsListFragment
import com.cramsan.petproject.plantslist.PlantsListFragment.Companion.ANIMAL_TYPE
import com.google.android.material.snackbar.Snackbar
import java.util.Date
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_cats
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_dogs
import kotlinx.android.synthetic.main.fragment_main_menu.plant_list_recycler
import kotlinx.android.synthetic.main.fragment_main_menu.plant_main_menu_list_view
import kotlinx.android.synthetic.main.fragment_main_menu.plant_main_menu_view
import kotlinx.android.synthetic.main.fragment_main_menu.plants_list_loading

class MainMenuFragment : BaseFragment<AllPlantListViewModel>(), AllPlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    override val logTag: String
        get() = "MainMenuFragment"
    override val contentViewLayout: Int
        get() = R.layout.fragment_main_menu

    private lateinit var plantsAdapter: AllPlantsRecyclerViewAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var downloadTime: Long = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model: AllPlantListViewModel by activityViewModels()
        main_menu_cats.setOnClickListener {
            if (!model.isCatalogReady()) {
                displayDownloadingMessage()
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), PlantsListActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, AnimalType.CAT.ordinal)
            startActivity(intent)
        }
        main_menu_dogs.setOnClickListener {
            if (!model.isCatalogReady()) {
                displayDownloadingMessage()
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), PlantsListActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, AnimalType.DOG.ordinal)
            startActivity(intent)
        }

        var startingOffset: Int? = null
        if (savedInstanceState != null) {
            startingOffset = savedInstanceState.getInt(PlantsListFragment.SCROLL_POS, 0)
        }

        layoutManager = LinearLayoutManager(context)
        plantsAdapter = AllPlantsRecyclerViewAdapter(this, AnimalType.ALL, requireContext())
        plant_list_recycler.layoutManager = layoutManager
        plant_list_recycler.adapter = plantsAdapter

        model.observablePlants().observe(viewLifecycleOwner, Observer { plants ->
            plantsAdapter.updateValues(plants)
        })
        model.observableLoading().observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                plants_list_loading.visibility = View.VISIBLE
                plant_list_recycler.visibility = View.GONE
            } else {
                plants_list_loading.visibility = View.GONE
                plant_list_recycler.visibility = View.VISIBLE
            }
        })
        model.observableInSearchMode().observe(viewLifecycleOwner, Observer {
            if (it) {
                plant_main_menu_view.visibility = View.GONE
                plant_main_menu_list_view.visibility = View.VISIBLE
            } else {
                plant_main_menu_view.visibility = View.VISIBLE
                plant_main_menu_list_view.visibility = View.GONE
            }
        })
        startingOffset?.let {
            layoutManager.scrollToPosition(startingOffset)
        }
        viewModel = model
    }

    override fun onStart() {
        super.onStart()
        if (viewModel?.isCatalogReady() != true) {
            metrics.log(TAG, "onStart", mapOf("FromCache" to "False"))
            viewModel?.observableDownloading()?.observe(requireActivity(), Observer<Boolean> { isLoading ->
                if (isLoading) {
                    return@Observer
                }
                val downloadCompleteTime = Date().time
                val downloadInSeconds = (downloadCompleteTime - downloadTime) / 1000
                if (downloadInSeconds < 1) {
                    metrics.log(TAG, "DownloadLatency", mapOf("Time" to "<1 second"))
                } else if (downloadInSeconds < 3) {
                    metrics.log(TAG, "DownloadLatency", mapOf("Time" to "<3 second"))
                } else if (downloadInSeconds < 5) {
                    metrics.log(TAG, "DownloadLatency", mapOf("Time" to "<5 second"))
                } else if (downloadInSeconds < 10) {
                    metrics.log(TAG, "DownloadLatency", mapOf("Time" to "<10 second"))
                } else if (downloadInSeconds < 20) {
                    metrics.log(TAG, "DownloadLatency", mapOf("Time" to "<20 second"))
                } else {
                    metrics.log(TAG, "DownloadLatency", mapOf("Time" to ">=20 second"))
                }
                displayDownloadCompleteMessage()
            })

            val intent = Intent(requireContext(), DownloadDialogActivity::class.java)
            startActivity(intent)
            downloadTime = Date().time
        } else {
            metrics.log(TAG, "onStart", mapOf("FromCache" to "True"))
        }
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

    companion object {
        const val TAG = "MainMenuFragment"
    }
}
