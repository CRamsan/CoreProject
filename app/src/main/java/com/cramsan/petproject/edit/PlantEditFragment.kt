package com.cramsan.petproject.edit

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.base.BaseFragment
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_banner_ad
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_common_names
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_danger
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_description
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_family
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image_loading
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image_source
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_scientific_name
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_source

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
