package com.cramsan.petproject.plantslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.plantdetails.PlantDetailsActivity.Companion.ANIMAL_TYPE

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlantsListFragment.OnListFragmentInteractionListener] interface.
 */
class PlantsListFragment : Fragment(), OnQueryTextListener {

    private var listener: OnListFragmentInteractionListener? = null
    private var plantsAdapter: PlantsRecyclerViewAdapter? = null
    private lateinit var model: PlantListViewModel
    private lateinit var animalType: AnimalType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreate")

        val animalTypeInt = arguments?.getInt(ANIMAL_TYPE, 0)
        animalTypeInt?.let {
            animalType = AnimalType.values()[animalTypeInt]
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onAttach")
        if (context is OnListFragmentInteractionListener) {
            listener = context
            listener?.onRegisterAsSearchable(this)
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreateView")
        val view = inflater.inflate(R.layout.fragment_plants_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                plantsAdapter = PlantsRecyclerViewAdapter(listener, animalType)
                adapter = plantsAdapter
            }
        }

        model = ViewModelProviders.of(this).get(PlantListViewModel::class.java)
        return view
    }

    override fun onResume() {
        super.onResume()
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onResume")
        model.observablePlants().observe(this, Observer<List<PresentablePlant>>{ plants ->
            plantsAdapter?.updateValues(plants)
        })
        model.reloadPlants(animalType)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onDetach")
        listener = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        CoreFrameworkAPI.eventLogger.log(Severity.DEBUG, classTag(), "onQueryTextSubmit")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        CoreFrameworkAPI.eventLogger.log(Severity.DEBUG, classTag(), "onQueryTextChange")
        newText?.let { model.searchPlants(it, animalType) }
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
        // TODO: Update argument type and name
        fun onListFragmentInteraction(plantId: Int, animalType: AnimalType)

        fun onRegisterAsSearchable(listener: OnQueryTextListener)
    }

    companion object {
        fun newInstace(animalType: AnimalType): PlantsListFragment {
            val args = Bundle()
            args.putInt(ANIMAL_TYPE, animalType.ordinal)
            val instance = PlantsListFragment()
            instance.arguments = args
            return instance
        }
    }
}
