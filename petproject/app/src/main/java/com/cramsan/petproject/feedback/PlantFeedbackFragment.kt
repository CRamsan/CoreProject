package com.cramsan.petproject.feedback

import android.os.Bundle
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

class PlantFeedbackFragment : BaseFragment() {

    private lateinit var viewModel: PlantFeedbackViewModel
    private lateinit var animalType: AnimalType
    private var plantId: Int = -1

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_feedback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        plantId = activity?.intent?.getIntExtra(PLANT_ID, -1) ?: return
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return
        animalType = AnimalType.values()[animalTypeId]
        viewModel = getViewModel(PlantFeedbackViewModel::class.java)
        viewModel.isComplete().observe(viewLifecycleOwner, Observer {
            if (it) {
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
            viewModel.sendFeedback(animalType, plantId.toLong(), photo, scientificName, name, brokenLikn, text)
        }
    }

    companion object {
        const val ANIMAL_TYPE = "animalType"
        const val PLANT_ID = "plantId"
    }
}
