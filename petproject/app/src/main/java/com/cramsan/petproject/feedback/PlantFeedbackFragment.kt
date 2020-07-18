package com.cramsan.petproject.feedback

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentPlantFeedbackBinding

class PlantFeedbackFragment : BaseFragment<PlantFeedbackViewModel, FragmentPlantFeedbackBinding>() {

    private lateinit var animalType: AnimalType
    private var plantId: Int = -1

    override val logTag: String
        get() = "PlantFeedbackFragment"
    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_feedback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        plantId = requireActivity().intent?.getIntExtra(PLANT_ID, -1) ?: return
        val animalTypeId = requireActivity().intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return
        animalType = AnimalType.values()[animalTypeId]

        val model: PlantFeedbackViewModel by viewModels()
        dataBinding.viewModel = model
        model.isComplete().observe(viewLifecycleOwner, Observer {
            if (it.feedbackSubmitted) {
                Toast.makeText(context, R.string.thanks_feedback, Toast.LENGTH_LONG).show()
            }
            requireActivity().finish()
        })
        viewModel = model
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val PLANT_ID = "plantId"
    }
}
