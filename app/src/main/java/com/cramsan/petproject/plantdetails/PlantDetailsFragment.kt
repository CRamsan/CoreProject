package com.cramsan.petproject.plantdetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cramsan.petproject.R

class PlantDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PlantDetailsFragment()
    }

    private lateinit var viewModel: PlantDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plant_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlantDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
