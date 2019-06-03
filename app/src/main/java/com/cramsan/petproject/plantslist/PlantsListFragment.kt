package com.cramsan.petproject.plantslist

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cramsan.petproject.R

/**
 * A placeholder fragment containing a simple view.
 */
class PlantsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plants_list, container, false)
    }
}
