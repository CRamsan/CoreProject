package com.cramsan.petproject.plantslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PresentablePlant

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PlantsListFragment.OnListFragmentInteractionListener] interface.
 */
class PlantsListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var plantsAdapter: PlantsRecyclerViewAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plants_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                plantsAdapter = PlantsRecyclerViewAdapter(listener)
                adapter = plantsAdapter
            }
        }

        val model = ViewModelProviders.of(this).get(PlantListViewModel::class.java)
        model.getPlants().observe(this, Observer<List<PresentablePlant>>{ plants ->
            plantsAdapter?.updateValues(plants)
        })
        model.reloadPlants()

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
    }
}
