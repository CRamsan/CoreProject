package com.cramsan.petproject.plantdetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.PetProjectApplication
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_common_names
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_danger
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_description
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_family
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_image
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_scientific_name
import kotlinx.android.synthetic.main.fragment_plant_details.plant_details_title
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantDetailsFragment : Fragment(), KodeinAware {

    override val kodein by lazy { (requireActivity().application as PetProjectApplication).kodein }
    private val eventLogger: EventLoggerInterface by instance()

    private lateinit var viewModel: PlantDetailsViewModel
    private lateinit var animalType: AnimalType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plant_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventLogger.log(Severity.INFO, classTag(), "onActivityCreated")
        val plantId = activity?.intent?.getIntExtra(PLANT_ID, -1) ?: return
        val animalTypeId = activity?.intent?.getIntExtra(ANIMAL_TYPE, -1) ?: return

        animalType = AnimalType.values()[animalTypeId]

        viewModel = ViewModelProviders.of(this).get(PlantDetailsViewModel::class.java)
        viewModel.getPlant().observe(this, Observer {
            plant_details_title.text = it.mainCommonName
            plant_details_scientific_name.text = getString(R.string.plant_details_scientific_name, it.exactName)
            plant_details_family.text = getString(R.string.plant_details_family, it.family)
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
                        return false
                    }
                })
                .override(plant_details_image.width, plant_details_image.height)
                .into(plant_details_image)
        })
        viewModel.getPlantMetadata().observe(this, Observer {
            if (it.isToxic) {
                plant_details_danger.text = when (animalType) {
                    AnimalType.CAT -> getString(R.string.plant_details_cat_dangerous)
                    AnimalType.DOG -> getString(R.string.plant_details_dog_dangerous)
                }

                plant_details_danger.setTextColor(resources.getColor(R.color.colorDanger, requireActivity().theme))
            } else {
                plant_details_danger.text = when (animalType) {
                    AnimalType.CAT -> getString(R.string.plant_details_cat_safe)
                    AnimalType.DOG -> getString(R.string.plant_details_dog_safe)
                }
                plant_details_danger.setTextColor(resources.getColor(R.color.colorSafe, requireActivity().theme))
            }
            plant_details_description.text = it.description
        })
        viewModel.reloadPlant(animalType, plantId)
    }

    companion object {
        const val PLANT_ID = "plantId"
        const val ANIMAL_TYPE = "animalType"
    }
}
