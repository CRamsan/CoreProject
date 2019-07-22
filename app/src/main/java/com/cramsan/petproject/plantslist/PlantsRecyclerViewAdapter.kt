package com.cramsan.petproject.plantslist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PresentablePlant

import com.cramsan.petproject.plantslist.PlantsListFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.view_plant.view.*

class PlantsRecyclerViewAdapter(
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<PlantsRecyclerViewAdapter.ViewHolder>() {

    private var mValues: List<PresentablePlant> = listOf()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PresentablePlant
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            mListener?.onListFragmentInteraction(item.plantId.toInt(), AnimalType.CAT)
        }
    }

    fun updateValues(values: List<PresentablePlant>) {
        mValues = values
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_plant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mViewHeader.text = item.mainCommonName
        holder.mViewSubHeader.text = item.scientificName
        holder.mViewImage.setImageResource(if (item.isToxic) R.drawable.is_toxic else R.drawable.is_not_toxic)

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
