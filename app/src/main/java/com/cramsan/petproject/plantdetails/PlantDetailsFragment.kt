package com.cramsan.petproject.plantdetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.cramsan.petproject.R
import com.cramsan.petproject.model.Plant
import kotlinx.android.synthetic.main.fragment_plant_details.*

class PlantDetailsFragment : Fragment() {

    private lateinit var viewModel: PlantDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plant_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val plantId = activity?.intent?.getStringExtra(PlantDetailsActivity.PLANT_ID) ?: return

        viewModel = ViewModelProviders.of(this).get(PlantDetailsViewModel::class.java)
        viewModel.getPlant().observe(this, Observer<Plant>{ plant ->
            plant_details_title.text = plant.exactName
            plant_details_subtitle.text = plant.commonName.joinToString { "," }
            plant_details_text.text = plant.family
            Glide.with(this)
                .load(plant.imageUrl)
                .into(plant_details_image)
        })
        viewModel.reloadPlant(plantId)
    }

}
