package com.cramsan.petproject.plantslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.PetProjectApplication
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import kotlinx.android.synthetic.main.fragment_plants_list.plant_list_recycler
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlantsListFragment.OnListFragmentInteractionListener] interface.
 */
class PlantsListFragment : Fragment(), OnQueryTextListener, KodeinAware {

    override val kodein by lazy { (requireActivity().application as PetProjectApplication).kodein }
    private val eventLogger: EventLoggerInterface by instance()

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var plantsAdapter: PlantsRecyclerViewAdapter
    private lateinit var model: PlantListViewModel
    private lateinit var animalType: AnimalType
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, classTag(), "onCreate")

        val animalTypeInt = arguments?.getInt(ANIMAL_TYPE, 0)
        animalTypeInt?.let {
            animalType = AnimalType.values()[animalTypeInt]
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventLogger.log(Severity.INFO, classTag(), "onAttach")
        if (context is OnListFragmentInteractionListener) {
            listener = context
            context.onRegisterAsSearchable(this)
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
        eventLogger.log(Severity.INFO, classTag(), "onCreateView")
        val view = inflater.inflate(R.layout.fragment_plants_list, container, false)

        layoutManager = LinearLayoutManager(context)
        plantsAdapter = PlantsRecyclerViewAdapter(listener, animalType, requireContext())
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                this.layoutManager = layoutManager
                this.adapter = plantsAdapter
            }
        }

        savedInstanceState?.let {
            val startingOffset = savedInstanceState.getInt(SCROLL_POS, 0)
            layoutManager.scrollToPosition(startingOffset)
        }

        model = ViewModelProviders.of(this).get(PlantListViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        plant_list_recycler.layoutManager = layoutManager
        plant_list_recycler.adapter = plantsAdapter
    }

    override fun onResume() {
        super.onResume()
        eventLogger.log(Severity.INFO, classTag(), "onResume")
        model.observablePlants().observe(this, Observer<List<PresentablePlant>> { plants ->
            plantsAdapter.updateValues(plants)
        })
        model.reloadPlants(animalType)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ANIMAL_TYPE, animalType.ordinal)
        outState.putInt(SCROLL_POS, layoutManager.findFirstVisibleItemPosition())

        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        eventLogger.log(Severity.INFO, classTag(), "onDetach")
        listener = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        eventLogger.log(Severity.DEBUG, classTag(), "onQueryTextSubmit")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        eventLogger.log(Severity.DEBUG, classTag(), "onQueryTextChange")
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
        fun onListFragmentInteraction(plantId: Int, animalType: AnimalType)

        fun onRegisterAsSearchable(listener: OnQueryTextListener)
    }

    class InvalidContextException(message: String?) : RuntimeException(message)

    companion object {
        fun newInstance(animalType: AnimalType): PlantsListFragment {
            val args = Bundle()
            args.putInt(ANIMAL_TYPE, animalType.ordinal)
            val instance = PlantsListFragment()
            instance.arguments = args
            return instance
        }

        const val ANIMAL_TYPE = "animalType"
        const val SCROLL_POS = "scrollPosition"
    }
}
