package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.network.models.content.Outfit
import java.util.ArrayList

class OutfitItemAdapter(private val context: Context, outfitList: List<Outfit>) : BaseAdapter() {
    private val mInflater: LayoutInflater
    private val outfitList: ArrayList<Outfit>

    init {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context)
        this.outfitList = ArrayList(outfitList)
    }

    override fun getCount(): Int {
        return this.outfitList.size
    }

    override fun getItem(position: Int): Outfit {
        return this.outfitList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // A ViewHolder keeps references to children views to avoid
        // unneccessary calls
        // to findViewById() on each row.
        val holder: ViewHolder

        // When convertView is not null, we can reuse it directly, there is
        // no need
        // to reinflate it. We only inflate a new View when the convertView
        // supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_outfit_item, parent, false)

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = ViewHolder()
            holder.outfitName =
                convertView!!.findViewById<View>(R.id.textViewOutfitName) as TextView
            holder.outfitAlias =
                convertView.findViewById<View>(R.id.textViewOutfitAlias) as TextView
            holder.memberCount =
                convertView.findViewById<View>(R.id.textViewOutfitCount) as TextView
            convertView.tag = holder
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as ViewHolder
        }

        holder.outfitName!!.text = this.outfitList[position].name
        holder.memberCount!!.text =
            context.resources.getString(R.string.text_outfit_members) + " " + this.outfitList[position].member_count
        val tag = this.outfitList[position].alias
        if (tag!!.length > 0) {
            holder.outfitAlias!!.text = "($tag)"
        }

        return convertView
    }

    internal class ViewHolder {
        var outfitName: TextView? = null
        var outfitAlias: TextView? = null
        var memberCount: TextView? = null
    }
}
