package com.cramsan.petproject.plantdetails

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.feedback.PlantFeedbackActivity
import com.cramsan.petproject.feedback.PlantFeedbackFragment
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_common_names
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_danger
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_description
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_family
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image_loading
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image_source
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_scientific_name
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_source
import kotlinx.android.synthetic.main.fragment_plant_details.plant_feedback_save

class PlantDetailsFragment : BaseFragment() {

    private var listener: OnDetailsFragmentInteractionListener? = null

    private lateinit var viewModel: PlantDetailsViewModel
    private lateinit var animalType: AnimalType

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_details
    override val logTag: String
        get() = "PlantDetailsFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDetailsFragmentInteractionListener) {
            listener = context
        } else {
            throw InvalidContextException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val plantId = activity?.intent?.getIntExtra(PLANT_ID, -1) ?: return
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]

        plant_details_image.visibility = View.INVISIBLE
        plant_details_image_loading.visibility = View.VISIBLE
        viewModel = getViewModel(PlantDetailsViewModel::class.java)
        viewModel.getPlant().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                eventLogger.log(Severity.WARNING, "PlantDetailsFragment", "Plant is null")
                return@Observer
            }

            listener?.onPlantReady(it)
            plant_details_scientific_name.text = getString(R.string.plant_details_scientific_name, it.exactName)
            plant_details_family.text = getString(R.string.plant_details_family, it.family)
            plant_details_image_source.text = getString(R.string.plant_details_source, it.imageUrl)
            plant_details_image_source.setOnClickListener { _ ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.imageUrl))
                startActivity(browserIntent)
            }
            it.commonNames.apply {
                if (isEmpty()) {
                    plant_details_common_names.visibility = View.GONE
                } else {
                    plant_details_common_names.visibility = View.VISIBLE
                    plant_details_common_names.text = getString(R.string.plant_details_common_names, it.commonNames)
                }
            }
            Glide.with(this)
                .load(it.imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        eventLogger.log(Severity.ERROR, "PlantDetailsFragment", e.toString())
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        eventLogger.log(Severity.VERBOSE, "PlantDetailsFragment",
                            "Resource loaded successfully")
                        plant_details_image.visibility = View.VISIBLE
                        plant_details_image_loading.visibility = View.INVISIBLE
                        return false
                    }
                })
                .override(plant_details_image.width, plant_details_image.height)
                .into(plant_details_image)
        })
        viewModel.getPlantMetadata().observe(viewLifecycleOwner, Observer { metadata ->
            if (metadata == null) {
                eventLogger.log(Severity.WARNING, "PlantDetailsFragment", "Metadata is null")
                return@Observer
            }

            listener?.onPlantMetadataReady(metadata)
            when (metadata.isToxic) {
                ToxicityValue.TOXIC -> {
                    plant_details_danger.text = when (animalType) {
                        AnimalType.CAT -> getString(R.string.plant_details_cat_dangerous)
                        AnimalType.DOG -> getString(R.string.plant_details_dog_dangerous)
                        AnimalType.ALL -> TODO()
                    }

                    plant_details_danger.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDanger))
                }
                ToxicityValue.NON_TOXIC -> {
                    plant_details_danger.text = when (animalType) {
                        AnimalType.CAT -> getString(R.string.plant_details_cat_safe)
                        AnimalType.DOG -> getString(R.string.plant_details_dog_safe)
                        AnimalType.ALL -> TODO()
                    }
                    plant_details_danger.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSafe))
                }
                ToxicityValue.UNDETERMINED -> {
                    plant_details_danger.text = when (animalType) {
                        AnimalType.CAT -> getString(R.string.plant_details_cat_unknown)
                        AnimalType.DOG -> getString(R.string.plant_details_dog_unknown)
                        AnimalType.ALL -> TODO()
                    }
                    plant_details_danger.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorUndetermined))
                }
            }
            if (metadata.description.isEmpty()) {
                plant_details_description.visibility = View.GONE
            }
            plant_details_description.text = metadata.description
            plant_details_source.text = getString(R.string.plant_details_source, metadata.source)
            plant_details_source.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(metadata.source))
                startActivity(browserIntent)
            }
        })

        plant_feedback_save.setOnClickListener {
            eventLogger.log(Severity.INFO, "PlantDetailsFragment", "onClick")
            val plantIntent = Intent(requireContext(), PlantFeedbackActivity::class.java)
            plantIntent.putExtra(PlantFeedbackFragment.PLANT_ID, plantId)
            plantIntent.putExtra(PlantFeedbackFragment.ANIMAL_TYPE, animalType.ordinal)
            startActivity(plantIntent)
        }

        viewModel.reloadPlant(animalType, plantId)
    }

    class InvalidContextException(message: String?) : RuntimeException(message)

    interface OnDetailsFragmentInteractionListener {
        fun onPlantReady(plant: Plant)
        fun onPlantMetadataReady(plantMetadata: PlantMetadata)
    }

    companion object {
        const val PLANT_ID = "plantId"
        const val ANIMAL_TYPE = "animalType"
    }
}
