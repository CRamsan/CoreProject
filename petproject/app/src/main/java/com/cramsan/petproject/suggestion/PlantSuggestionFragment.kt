package com.cramsan.petproject.suggestion

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentPlantSuggestionBinding

class PlantSuggestionFragment : BaseFragment<PlantSuggestionViewModel, FragmentPlantSuggestionBinding>() {

    private lateinit var animalType: AnimalType

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_suggestion
    override val logTag: String
        get() = "PlantSuggestionFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]
        val model: PlantSuggestionViewModel by viewModels()
        dataBinding.viewModel = model
        model.observableIsComplete.observe(
            viewLifecycleOwner,
            Observer {
                if (it.suggestionSubmitted) {
                    Toast.makeText(context, R.string.thanks_suggestion, Toast.LENGTH_LONG).show()
                }
                requireActivity().finish()
            }
        )

        viewModel = model
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
    }
}
