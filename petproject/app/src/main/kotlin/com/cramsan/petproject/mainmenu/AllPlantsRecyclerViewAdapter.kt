package com.cramsan.petproject.mainmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logV
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.model.ToxicityValue

/**
 * Adapter that displays the list of all Plants.
 */
class AllPlantsRecyclerViewAdapter(
    private val mListener: OnListFragmentAdapterListener?,
    private val animalType: AnimalType,
) :
    RecyclerView.Adapter<AllPlantsRecyclerViewAdapter.ViewHolder>() {

    private var mValues: List<PresentablePlant> = listOf()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PresentablePlant
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            mListener?.onNewItemSelected(item.plantId.toInt(), item.animalType)
        }
    }

    /**
     * Set the content of this adapter.
     */
    fun updateValues(values: List<PresentablePlant>) {
        logV("AllPlantsRecyclerViewAdapter", "updateValues")
        mValues = values
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        logD("AllPlantsRecyclerViewAdapter", "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_plant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        logV("AllPlantsRecyclerViewAdapter", "onBindViewHolder")
        val item = mValues[position]
        holder.mViewHeader.text = item.mainCommonName
        holder.mViewSubHeader.text = item.scientificName
        when (item.animalType) {
            AnimalType.CAT -> {
                holder.mViewIconCat.visibility = View.VISIBLE
                holder.mViewIconDog.visibility = View.GONE
            }
            AnimalType.DOG -> {
                holder.mViewIconCat.visibility = View.GONE
                holder.mViewIconDog.visibility = View.VISIBLE
            }
            else -> {
                holder.mViewIconCat.visibility = View.GONE
                holder.mViewIconDog.visibility = View.GONE
            }
        }

        val targetAnimalView = when (item.animalType) {
            AnimalType.CAT -> holder.mViewIconCat
            AnimalType.DOG -> holder.mViewIconDog
            else -> TODO()
        }

        when (item.isToxic) {
            ToxicityValue.TOXIC -> targetAnimalView.setBackgroundResource(R.drawable.plant_view_item_danger)
            ToxicityValue.NON_TOXIC -> targetAnimalView.setBackgroundResource(R.drawable.plant_view_item_safe)
            ToxicityValue.UNDETERMINED -> targetAnimalView.setBackgroundResource(R.drawable.plant_view_item_undetermined)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    /**
     * ViewHolder that keeps a reference to the view that will be recycled.
     */
    inner class ViewHolder(
        /**
         * View that is tied to this holder.
         */
        val mView: View
    ) : RecyclerView.ViewHolder(mView) {
        /**
         * Reference to the header text.
         */
        val mViewHeader: TextView = mView.findViewById(R.id.plant_list_view_header)
        /**
         * Reference to the subheader text.
         */
        val mViewSubHeader: TextView = mView.findViewById(R.id.plant_list_view_sub_header)
        /**
         * Reference to the thumbnail of the cat.
         */
        val mViewIconCat: ImageView = mView.findViewById(R.id.plant_list_view_icon_cat)
        /**
         * Reference to the thumbnail of the dog.
         */
        val mViewIconDog: ImageView = mView.findViewById(R.id.plant_list_view_icon_dog)
        /**
         * Reference to the container layout.
         */
        val mContainerView: View = mView.findViewById(R.id.plant_list_view_layout)

        override fun toString(): String {
            return super.toString() + " '" + mViewHeader.text + "'"
        }
    }

    /**
     * Listener for events from [AllPlantsRecyclerViewAdapter].
     */
    interface OnListFragmentAdapterListener {
        /**
         * New item selected.
         */
        fun onNewItemSelected(plantId: Int, animalType: AnimalType)
    }
}
