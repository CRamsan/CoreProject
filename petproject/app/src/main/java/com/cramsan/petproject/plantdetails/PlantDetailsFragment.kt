package com.cramsan.petproject.plantdetails

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.databinding.FragmentPlantDetailsBinding
import com.cramsan.petproject.feedback.PlantFeedbackActivity

class PlantDetailsFragment : BaseFragment<PlantDetailsViewModel, FragmentPlantDetailsBinding>() {

    private lateinit var animalType: AnimalType

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_details
    override val logTag: String
        get() = "PlantDetailsFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val plantId = activity?.intent?.getIntExtra(PLANT_ID, -1) ?: return
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]

        val model: PlantDetailsViewModel by activityViewModels()
        dataBinding.viewModel = model

        model.observableOpenSourceLink().observe(viewLifecycleOwner, Observer {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.value))
            startActivity(browserIntent)
        })
        model.observablePlantImageSource.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it)
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
                        dataBinding.plantDetailsImageLoading.visibility = View.GONE
                        dataBinding.plantDetailsImage.visibility = View.VISIBLE
                        return false
                    }
                })
                .override(dataBinding.plantDetailsImage.width, dataBinding.plantDetailsImage.height)
                .into(dataBinding.plantDetailsImage)
        })

        dataBinding.plantFeedbackSave.setOnClickListener {
            eventLogger.log(Severity.INFO, "PlantDetailsFragment", "onClick")
            val plantIntent = Intent(requireContext(), PlantFeedbackActivity::class.java)
            plantIntent.putExtra(PLANT_ID, plantId)
            plantIntent.putExtra(ANIMAL_TYPE, animalType.ordinal)
            startActivity(plantIntent)
        }

        model.reloadPlant(animalType, plantId)
        viewModel = model
    }

    companion object {
        const val PLANT_ID = "plantId"
        const val ANIMAL_TYPE = "animalType"
    }
}
