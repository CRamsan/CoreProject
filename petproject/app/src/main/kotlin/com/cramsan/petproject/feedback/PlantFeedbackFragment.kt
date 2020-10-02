package com.cramsan.petproject.feedback

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseDialogFragment
import com.cramsan.petproject.databinding.FragmentPlantFeedbackBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantFeedbackFragment : BaseDialogFragment<PlantFeedbackViewModel, FragmentPlantFeedbackBinding>() {

    private lateinit var animalType: AnimalType
    private var plantId: Int = -1

    override val logTag: String
        get() = "PlantFeedbackFragment"
    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_feedback

    val args: PlantFeedbackFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        plantId = args.PlantId
        animalType = args.AnimalType

        val model: PlantFeedbackViewModel by viewModels()
        dataBinding.viewModel = model
        model.isComplete().observe(
            viewLifecycleOwner,
            Observer {
                if (it.feedbackSubmitted) {
                    Toast.makeText(context, R.string.thanks_feedback, Toast.LENGTH_LONG).show()
                }
                closeDialog()
            }
        )
        viewModel = model
    }

    private fun closeDialog() {
        val action = PlantFeedbackFragmentDirections.actionPlantFeedbackFragmentPop()
        findNavController().navigate(action)
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val PLANT_ID = "plantId"
    }
}
