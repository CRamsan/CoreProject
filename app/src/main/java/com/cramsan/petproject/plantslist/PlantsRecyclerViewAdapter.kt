package com.cramsan.petproject.plantslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.plantslist.PlantsListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.view_plant.view.viewPlantHeader
import kotlinx.android.synthetic.main.view_plant.view.viewPlantImage
import kotlinx.android.synthetic.main.view_plant.view.viewPlantSubHeader
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class PlantsRecyclerViewAdapter(
    private val mListener: OnListFragmentInteractionListener?,
    private val animalType: AnimalType,
    context: Context
) :
    RecyclerView.Adapter<PlantsRecyclerViewAdapter.ViewHolder>(), KodeinAware {

    override val kodein by org.kodein.di.android.kodein(context)
    private val eventLogger: EventLoggerInterface by instance()

    private var mValues: List<PresentablePlant> = listOf()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PresentablePlant
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            mListener?.onListFragmentInteraction(item.plantId.toInt(), animalType)
        }
    }

    fun updateValues(values: List<PresentablePlant>) {
        eventLogger.log(Severity.VERBOSE, classTag(), "updateValues")
        mValues = values
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        eventLogger.log(Severity.DEBUG, classTag(), "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_plant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        eventLogger.log(Severity.VERBOSE, classTag(), "onBindViewHolder")
        val item = mValues[position]
        holder.mViewHeader.text = item.mainCommonName
        holder.mViewSubHeader.text = item.scientificName
        when (item.isToxic) {
            ToxicityValue.TOXIC -> holder.mViewImage.setImageResource(R.drawable.is_toxic)
            ToxicityValue.NON_TOXIC -> holder.mViewImage.setImageResource(R.drawable.is_not_toxic)
            ToxicityValue.UNDETERMINED -> holder.mViewImage.setImageResource(R.drawable.is_undetermined_toxic)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mViewHeader: TextView = mView.viewPlantHeader
        val mViewSubHeader: TextView = mView.viewPlantSubHeader
        val mViewImage: ImageView = mView.viewPlantImage

        override fun toString(): String {
            return super.toString() + " '" + mViewHeader.text + "'"
        }
    }
}
