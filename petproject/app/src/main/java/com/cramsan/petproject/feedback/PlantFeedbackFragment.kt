package com.cramsan.petproject.feedback

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_plant_feedback.plantFeedbackBrokenLinkCheckBox
import kotlinx.android.synthetic.main.fragment_plant_feedback.plantFeedbackNameCheckBox
import kotlinx.android.synthetic.main.fragment_plant_feedback.plantFeedbackPhotoCheckBox
import kotlinx.android.synthetic.main.fragment_plant_feedback.plantFeedbackScientificNameCheckBox
import kotlinx.android.synthetic.main.fragment_plant_feedback.plantFeedbackText
import kotlinx.android.synthetic.main.fragment_plant_feedback.plant_feedback_cancel
import kotlinx.android.synthetic.main.fragment_plant_feedback.plant_feedback_save

class PlantFeedbackFragment : BaseFragment<PlantFeedbackViewModel>() {

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
        model.isComplete().observe(viewLifecycleOwner, Observer { isComplete ->
            if (isComplete) {
                requireActivity().finish()
            }
        })

        plant_feedback_cancel.setOnClickListener {
            requireActivity().finish()
        }

        plant_feedback_save.setOnClickListener {
            val photo = plantFeedbackPhotoCheckBox.isChecked
            val scientificName = plantFeedbackScientificNameCheckBox.isChecked
            val name = plantFeedbackNameCheckBox.isChecked
            val brokenLikn = plantFeedbackBrokenLinkCheckBox.isChecked
            val text = plantFeedbackText.text.toString()
            model.sendFeedback(animalType, plantId.toLong(), photo, scientificName, name, brokenLikn, text)
        }
        viewModel = model
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val PLANT_ID = "plantId"
    }
}
