package com.cramsan.petproject.plantdetails

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

class MainMenuFragment : BaseFragment() {

    private var listener: OnDetailsFragmentInteractionListener? = null

    private lateinit var viewModel: MainMenuViewModel
    private lateinit var animalType: AnimalType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_plant_details, container, false)
    }

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
        viewModel = ViewModelProviders.of(this).get(MainMenuViewModel::class.java)
        viewModel.getPlant().observe(this, Observer {
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
                        eventLogger.log(Severity.ERROR, classTag(), e.toString())
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        eventLogger.log(Severity.VERBOSE, classTag(),
                            "Resource loaded successfully")
                        plant_details_image.visibility = View.VISIBLE
                        plant_details_image_loading.visibility = View.INVISIBLE
                        return false
                    }
                })
                .override(plant_details_image.width, plant_details_image.height)
                .into(plant_details_image)
        })
        viewModel.getPlantMetadata().observe(this, Observer { metadata ->
            listener?.onPlantMetadataReady(metadata)
            when (metadata.isToxic) {
                ToxicityValue.TOXIC -> {
                    plant_details_danger.text = when (animalType) {
                        AnimalType.CAT -> getString(R.string.plant_details_cat_dangerous)
                        AnimalType.DOG -> getString(R.string.plant_details_dog_dangerous)
                    }

                    plant_details_danger.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDanger))
                }
                ToxicityValue.NON_TOXIC -> {
                    plant_details_danger.text = when (animalType) {
                        AnimalType.CAT -> getString(R.string.plant_details_cat_safe)
                        AnimalType.DOG -> getString(R.string.plant_details_dog_safe)
                    }
                    plant_details_danger.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSafe))
                }
                ToxicityValue.UNDETERMINED -> {
                    plant_details_danger.text = when (animalType) {
                        AnimalType.CAT -> getString(R.string.plant_details_cat_unknown)
                        AnimalType.DOG -> getString(R.string.plant_details_dog_unknown)
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
        viewModel.reloadPlant(animalType, plantId)

        val mAdView = plant_details_banner_ad
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
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
