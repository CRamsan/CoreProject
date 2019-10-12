package com.cramsan.petproject.edit

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_plant_edit.*

class PlantEditFragment : BaseFragment() {

    private lateinit var viewModel: PlantEditViewModel
    private lateinit var animalType: AnimalType

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_edit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]
        viewModel = ViewModelProviders.of(this).get(PlantEditViewModel::class.java)
        viewModel.isComplete().observe(this, Observer {
            if (it) {
                requireActivity().finish()
            }
        })

        viewModel.isLoading().observe(this, Observer {
            plant_edit_save.isEnabled = !it
        })

        plant_edit_save.setOnClickListener {
            val plantName = plant_edit_main_name.text.toString()
            val plantScientificName = plant_edit_scientific_name.text.toString()
            val plantFamily = plant_edit_family.text.toString()
            var toxicityForCats = ToxicityValue.UNDETERMINED
            if (plant_edit_cat_safe.isChecked) {
                toxicityForCats = ToxicityValue.NON_TOXIC
            } else if (plant_edit_cat_unsafe.isChecked) {
                toxicityForCats = ToxicityValue.TOXIC
            }
            var toxicityForDogs = ToxicityValue.UNDETERMINED
            if (plant_edit_dog_safe.isChecked) {
                toxicityForDogs = ToxicityValue.NON_TOXIC
            } else if (plant_edit_dog_unsafe.isChecked) {
                toxicityForDogs = ToxicityValue.TOXIC
            }
            viewModel.savePlant(plantName, plantScientificName, plantFamily, toxicityForCats, toxicityForDogs)
        }
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
    }
}
