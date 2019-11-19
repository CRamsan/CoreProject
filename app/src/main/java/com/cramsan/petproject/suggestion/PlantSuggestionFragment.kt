package com.cramsan.petproject.suggestion

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_plant_suggestion.*

class PlantSuggestionFragment : BaseFragment() {

    private lateinit var viewModel: PlantSuggestionViewModel
    private lateinit var animalType: AnimalType

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_suggestion

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]
        viewModel = ViewModelProviders.of(this).get(PlantSuggestionViewModel::class.java)
        viewModel.isComplete().observe(this, Observer {
            if (it) {
                requireActivity().finish()
            }
        })

        plant_suggestion_cancel.setOnClickListener {
            requireActivity().finish()
        }

        plant_suggestion_save.setOnClickListener {
            val plantName = plant_suggestion_scientific_name.text.toString()
            var toxicityForCats = ToxicityValue.UNDETERMINED
            if (plant_suggestion_cat_safe.isChecked) {
                toxicityForCats = ToxicityValue.NON_TOXIC
            } else if (plant_suggestion_cat_unsafe.isChecked) {
                toxicityForCats = ToxicityValue.TOXIC
            }
            var toxicityForDogs = ToxicityValue.UNDETERMINED
            if (plant_suggestion_dog_safe.isChecked) {
                toxicityForDogs = ToxicityValue.NON_TOXIC
            } else if (plant_suggestion_dog_unsafe.isChecked) {
                toxicityForDogs = ToxicityValue.TOXIC
            }
            viewModel.savePlant(plantName, toxicityForCats, toxicityForDogs)
        }
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
    }
}
