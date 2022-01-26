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
import com.cramsan.framework.core.BaseDatabindingFragment
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logV
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.databinding.FragmentPlantDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 */
@AndroidEntryPoint
class PlantDetailsFragment : BaseDatabindingFragment<PlantDetailsViewModel, FragmentPlantDetailsBinding>() {

    private lateinit var animalType: AnimalType

    override val viewModel: PlantDetailsViewModel by viewModels()
    override val contentViewLayout: Int
        get() = R.layout.fragment_plant_details
    override val logTag: String
        get() = "PlantDetailsFragment"

    private val args: PlantDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val plantId = args.PlantId
        animalType = args.AnimalType

        dataBinding.viewModel = viewModel

        viewModel.observableOpenSourceLink().observe(
            viewLifecycleOwner,
            Observer {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.value))
                startActivity(browserIntent)
            }
        )

        viewModel.observablePlantImageSource.observe(
            viewLifecycleOwner,
            Observer {
                Glide.with(this)
                    .load(it)
                    .listener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                logE("PlantDetailsFragment", e.toString())
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                logV(
                                    "PlantDetailsFragment",
                                    "Resource loaded successfully"
                                )
                                dataBinding.plantDetailsImageLoading.visibility = View.GONE
                                dataBinding.plantDetailsImage.visibility = View.VISIBLE
                                return false
                            }
                        }
                    )
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
            logI("PlantDetailsFragment", "onClick")
            val action = PlantDetailsFragmentDirections.actionPlantDetailsFragmentToPlantFeedbackFragment(animalType, plantId)
            findNavController().navigate(action)
        }

        viewModel.reloadPlant(animalType, plantId)
    }
}
