package com.cramsan.petproject.plantslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.plantdetails.PlantDetailsActivity
import com.cramsan.petproject.plantdetails.PlantDetailsFragment.Companion.PLANT_ID
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_plants_list.plant_list_banner_ad
import kotlinx.android.synthetic.main.fragment_plants_list.plant_list_recycler
import kotlinx.android.synthetic.main.fragment_plants_list.plants_list_loading
import org.kodein.di.erased.instance

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlantsListFragment.OnListFragmentInteractionListener] interface.
 */
class PlantsListFragment : BaseFragment(), SearchView.OnQueryTextListener,
    PlantsRecyclerViewAdapter.OnListFragmentAdapterListener {

    private val preferences: PreferencesInterface by instance()

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var plantsAdapter: PlantsRecyclerViewAdapter
    private lateinit var model: PlantListViewModel
    private lateinit var animalType: AnimalType
    private lateinit var layoutManager: LinearLayoutManager
    private var searchQuery: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
            context.onNewSearchable(this)
        } else {
            throw InvalidContextException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_plants_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mAdView = plant_list_banner_ad
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        var animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1)
        var startingOffset: Int? = null
        if (savedInstanceState != null) {
            startingOffset = savedInstanceState.getInt(SCROLL_POS, 0)
            searchQuery = savedInstanceState.getString(SEARCH_QUERY)
            animalTypeId = savedInstanceState.getInt(ANIMAL_TYPE, -1)
        }

        if (animalTypeId == null || animalTypeId == -1) {
            animalTypeId = preferences.loadInt(ANIMAL_TYPE)
        }
        if (animalTypeId == null) {
            animalTypeId = AnimalType.CAT.ordinal
        }
        animalType = AnimalType.values()[animalTypeId]
        preferences.saveInt(ANIMAL_TYPE, animalTypeId)

        layoutManager = LinearLayoutManager(context)
        plantsAdapter = PlantsRecyclerViewAdapter(this, animalType, requireContext())
        plant_list_recycler.layoutManager = layoutManager
        plant_list_recycler.adapter = plantsAdapter

        model = ViewModelProviders.of(this).get(PlantListViewModel::class.java)
        model.animalType = animalType
        model.observablePlants().observe(this, Observer<List<PresentablePlant>> { plants ->
            plantsAdapter.updateValues(plants)
        })
        model.observableLoading().observe(this, Observer<Boolean> { isLoading ->
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
        val loadedSearchQuery = searchQuery
        if (loadedSearchQuery?.isNotBlank() == true) {
            model.searchPlants(loadedSearchQuery)
        } else {
            model.reloadPlants()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        outState.putInt(SCROLL_POS, layoutManager.findFirstVisibleItemPosition())
        outState.putString(SEARCH_QUERY, searchQuery)

        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onNewItemSelected(plantId: Int, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, classTag(), "onNewItemSelected")
        val plantIntent = Intent(requireContext(), PlantDetailsActivity::class.java)
        plantIntent.putExtra(PLANT_ID, plantId)
        plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
        startActivity(plantIntent)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        eventLogger.log(Severity.DEBUG, classTag(), "onQueryTextSubmit")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        eventLogger.log(Severity.DEBUG, classTag(), "onQueryTextChange")
        newText?.let {
            if (newText.length > 1) {
                searchQuery = newText
            }
            model.searchPlants(it)
        }
        return true
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onNewSearchable(listener: SearchView.OnQueryTextListener)
    }

    class InvalidContextException(message: String?) : RuntimeException(message)

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val SEARCH_QUERY = "searchQuery"
        const val SCROLL_POS = "scrollPosition"
    }
}
