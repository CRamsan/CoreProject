package com.cramsan.petproject.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.base.BaseFragment

class PlantEditFragment : BaseFragment() {

    private var editener: OnEditFragmentInteractionListener? = null

    private lateinit var viewModel: PlantEditViewModel
    private lateinit var animalType: AnimalType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_plant_edit, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditFragmentInteractionListener) {
            editener = context
        } else {
            throw InvalidContextException("$context must implement OnEditFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val plantId = activity?.intent?.getIntExtra(PLANT_ID, -1) ?: return
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]
    }

    class InvalidContextException(message: String?) : RuntimeException(message)

    interface OnEditFragmentInteractionListener {
        fun onPlantReady(plant: Plant)
        fun onPlantMetadataReady(plantMetadata: PlantMetadata)
    }

    companion object {
        const val PLANT_ID = "plantId"
        const val ANIMAL_TYPE = "animalType"
    }
}
