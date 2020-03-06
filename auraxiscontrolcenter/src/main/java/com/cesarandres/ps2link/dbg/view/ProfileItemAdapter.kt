package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.content.CharacterProfile
import com.cesarandres.ps2link.dbg.content.Faction

import java.util.ArrayList

class ProfileItemAdapter(
    context: Context,
    profileList: List<CharacterProfile>,
    private val full: Boolean
) : BaseAdapter() {
    private val mInflater: LayoutInflater
    private val profileList: ArrayList<CharacterProfile>

    init {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context)
        this.profileList = ArrayList(profileList)
        vs_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_faction_vs)
        tr_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_faction_tr)
        nc_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_faction_nc)
    }

    override fun getCount(): Int {
        return this.profileList.size
    }

    override fun getItem(position: Int): CharacterProfile {
        return this.profileList[position]
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
            convertView = mInflater.inflate(R.layout.layout_profile_item, parent, false)

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = ViewHolder()
            holder.faction = convertView!!.findViewById<View>(R.id.imageViewFaction) as ImageView
            holder.battleRank = convertView.findViewById<View>(R.id.textViewBattleRank) as TextView
            holder.battleRank!!.visibility = View.VISIBLE
            holder.name =
                convertView.findViewById<View>(R.id.textViewProfileCharacterName) as TextView
            convertView.tag = holder
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as ViewHolder
        }

        // Bind the data efficiently with the holder.
        val profile = this.profileList[position]
        if (this.full) {
            if (Faction.VS == profile.faction_id) {
                holder.faction!!.setImageBitmap(vs_icon)
            } else if (Faction.NC == profile.faction_id) {
                holder.faction!!.setImageBitmap(nc_icon)
            } else if (Faction.TR == profile.faction_id) {
                holder.faction!!.setImageBitmap(tr_icon)
            }
            holder.battleRank!!.text = Integer.toString(profile.battle_rank!!.value)
        } else {
            if (profile.character_id_join_character == null) {
                holder.faction!!.setImageDrawable(null)
            } else {
                if (Faction.VS == profile.character_id_join_character!!.faction_id) {
                    holder.faction!!.setImageBitmap(vs_icon)
                } else if (Faction.NC == profile.character_id_join_character!!.faction_id) {
                    holder.faction!!.setImageBitmap(nc_icon)
                } else if (Faction.TR == profile.character_id_join_character!!.faction_id) {
                    holder.faction!!.setImageBitmap(tr_icon)
                }
            }
            if (profile.character_id_join_character == null) {
                holder.battleRank!!.text = "-"
            } else {
                holder.battleRank!!.text =
                    Integer.toString(profile.character_id_join_character!!.battle_rank!!.value)
            }
        }
        holder.name!!.text = this.profileList[position].name!!.first

        return convertView
    }

    internal class ViewHolder {
        var faction: ImageView? = null
        var battleRank: TextView? = null
        var name: TextView? = null
    }

    companion object {
        private lateinit var vs_icon: Bitmap
        private lateinit var nc_icon: Bitmap
        private lateinit var tr_icon: Bitmap
    }
}