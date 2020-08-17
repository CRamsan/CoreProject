package com.cramsan.petproject.plantdetails

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

class PlantDetailsFragment : BaseFragment<PlantDetailsViewModel, FragmentPlantDetailsBinding>() {

    private lateinit var animalType: AnimalType

    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_details
    override val logTag: String
        get() = "PlantDetailsFragment"

    val args: PlantDetailsFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val plantId = args.PlantId
        animalType = args.AnimalType

        val model: PlantDetailsViewModel by viewModels()
        dataBinding.viewModel = model
        viewModel = model

        model.observableOpenSourceLink().observe(
            viewLifecycleOwner,
            Observer {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.value))
                startActivity(browserIntent)
            }
        )

        model.observablePlantImageSource.observe(
            viewLifecycleOwner,
            Observer {
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
                            eventLogger.log(
                                Severity.VERBOSE, "PlantDetailsFragment",
                                "Resource loaded successfully"
                            )
                            dataBinding.plantDetailsImageLoading.visibility = View.GONE
                            dataBinding.plantDetailsImage.visibility = View.VISIBLE
                            return false
                        }
                    })
                    .override(dataBinding.plantDetailsImage.width, dataBinding.plantDetailsImage.height)
                    .into(dataBinding.plantDetailsImage)
            }
        )

        viewModel.observableStartDownload().observe(
            viewLifecycleOwner,
            Observer {
                val action = PlantDetailsFragmentDirections.actionPlantDetailsFragmentToDownloadCatalogDialogFragment()
                findNavController().navigate(action)
            }
        )

        viewModel.observablePlantName.observe(
            viewLifecycleOwner,
            Observer {
                val activity = requireActivity() as AppCompatActivity
                activity.supportActionBar?.title = it
            }
        )

        dataBinding.plantFeedbackSave.setOnClickListener {
            eventLogger.log(Severity.INFO, "PlantDetailsFragment", "onClick")
            val action = PlantDetailsFragmentDirections.actionPlantDetailsFragmentToPlantFeedbackFragment(animalType, plantId)
            findNavController().navigate(action)
        }

        model.reloadPlant(animalType, plantId)
    }

    companion object {
        const val PLANT_ID = "plantId"
        const val ANIMAL_TYPE = "animalType"
    }
}
