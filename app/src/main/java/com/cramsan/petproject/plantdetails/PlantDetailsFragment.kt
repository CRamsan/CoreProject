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
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
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
        val plantId = activity?.intent?.getIntExtra(PlantDetailsActivity.PLANT_ID, -1) ?: return

        viewModel = ViewModelProviders.of(this).get(PlantDetailsViewModel::class.java)
        viewModel.getPlant().observe(this, Observer {
            plant_details_title.text = it.mainCommonName
            plant_details_scientific_name.text = it.exactName
            plant_details_common_names.text = it.commonNames
            plant_details_family.text = it.family
            Glide.with(this)
                .load(it.imageUrl)
                .into(plant_details_image)
        })
        viewModel.getPlantMetadata().observe(this, Observer {
            plant_details_danger.text = it.isToxic.toString()
            plant_details_description.text = it.description
        })
        viewModel.reloadPlant(AnimalType.CAT, plantId)
    }

}
