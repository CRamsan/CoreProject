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
import com.android.volley.toolbox.NetworkImageView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.volley.GsonRequest
import com.cramsan.ps2link.network.models.content.Faction
import com.cramsan.ps2link.network.models.content.item.WeaponStat
import com.cramsan.ps2link.network.models.content.response.Item_list_response
import java.util.Hashtable

class WeaponItemAdapter(
    private val context: Context,
    private val weaponKills: List<WeaponStat>,
    private val weaponKilledBy: List<WeaponStat>,
    private val characterFaction: String,
    var isMyWeapons: Boolean,
) : BaseAdapter() {

    protected var mInflater: LayoutInflater
    private val icon_auraxium: Bitmap
    private val icon_gold: Bitmap
    private val icon_silver: Bitmap
    private val icon_copper: Bitmap
    private val icon_empty: Bitmap

    private val requestTable: Hashtable<View, GsonRequest<Item_list_response>>

    init {
        this.mInflater = LayoutInflater.from(context)
        requestTable = Hashtable(20)
        icon_auraxium = BitmapFactory.decodeResource(context.resources, R.drawable.medal_araxium)
        icon_gold = BitmapFactory.decodeResource(context.resources, R.drawable.medal_gold)
        icon_silver = BitmapFactory.decodeResource(context.resources, R.drawable.medal_silver)
        icon_copper = BitmapFactory.decodeResource(context.resources, R.drawable.medal_copper)
        icon_empty = BitmapFactory.decodeResource(context.resources, R.drawable.medal_empty)
    }

    override fun getCount(): Int {
        return if (this.isMyWeapons) {
            this.weaponKills.size
        } else {
            this.weaponKilledBy.size
        }
    }

    override fun getItem(position: Int): WeaponStat {
        return if (this.isMyWeapons) {
            this.weaponKills[position]
        } else {
            this.weaponKilledBy[position]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_weapon_item, parent, false)

            holder = ViewHolder()
            holder.name = convertView!!.findViewById<View>(R.id.TextViewWeaponItemName) as TextView
            holder.weaponImage =
                convertView.findViewById<View>(R.id.ImageViewWeaponItemImage) as NetworkImageView
            holder.weaponImage!!.setErrorImageResId(R.drawable.image_not_found)
            holder.kills = convertView.findViewById<View>(R.id.TextViewWeaponItemKiils) as TextView
            holder.ratios = convertView.findViewById<View>(R.id.TextViewWeaponItemRatio) as TextView
            holder.headshots =
                convertView.findViewById<View>(R.id.TextViewWeaponItemHeadshots) as TextView
            holder.vehiclekills =
                convertView.findViewById<View>(R.id.TextViewWeaponItemVehicleKills) as TextView
            holder.medal =
                convertView.findViewById<View>(R.id.imageViewWeaponItemMedal) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            val request = requestTable[convertView]
            request?.cancel()
        }

        val stat = getItem(position)

        if (stat.vehicle != null) {
            if (stat.vehicle == stat.name) {
                holder.name!!.text = stat.name
            } else {
                holder.name!!.text = stat.name + "(" + stat.vehicle + ")"
            }
        } else {
            holder.name!!.text = stat.name
        }

        if (this.isMyWeapons) {
            holder.medal!!.visibility = View.VISIBLE
            if (stat.kills < 10) {
                holder.medal!!.setImageBitmap(icon_empty)
            } else if (stat.kills < 60) {
                holder.medal!!.setImageBitmap(icon_copper)
            } else if (stat.kills < 160) {
                holder.medal!!.setImageBitmap(icon_silver)
            } else if (stat.kills <= 1160) {
                holder.medal!!.setImageBitmap(icon_gold)
            } else {
                holder.medal!!.setImageBitmap(icon_auraxium)
            }

            holder.kills!!.text =
                this.context.resources.getString(R.string.text_kills) + " " + stat.kills

            holder.ratios!!.visibility = View.VISIBLE
            if (this.characterFaction == Faction.VS) {
                holder.ratios!!.text =
                    this.context.resources.getString(R.string.text_nc_) + " " + Math.round(100 * stat.nc / stat.kills.toFloat()) +
                    "% " + this.context.resources.getString(R.string.text_tr_) + " " + Math.round(100 * stat.tr / stat.kills.toFloat()) + "%"
            } else if (this.characterFaction == Faction.NC) {
                holder.ratios!!.text =
                    this.context.resources.getString(R.string.text_tr_) + " " + Math.round(100 * stat.tr / stat.kills.toFloat()) +
                    "% " + this.context.resources.getString(R.string.text_vs_) + " " + Math.round(100 * stat.vs / stat.kills.toFloat()) + "%"
            } else if (this.characterFaction == Faction.TR) {
                holder.ratios!!.text =
                    this.context.resources.getString(R.string.text_nc_) + " " + Math.round(100 * stat.nc / stat.kills.toFloat()) +
                    "% " + this.context.resources.getString(R.string.text_vs_) + " " + Math.round(100 * stat.vs / stat.kills.toFloat()) + "%"
            }

            holder.headshots!!.visibility = View.VISIBLE
            holder.headshots!!.text =
                this.context.resources.getString(R.string.text_headshots_) + " " + stat.headshots

            holder.vehiclekills!!.visibility = View.VISIBLE
            if (stat.vehicleKills > 0) {
                holder.vehiclekills!!.text =
                    this.context.resources.getString(R.string.text_vehicle_kills_) + " " + stat.vehicleKills
            } else {
                holder.vehiclekills!!.text = ""
            }
        } else {
            holder.medal!!.visibility = View.GONE
            holder.ratios!!.visibility = View.GONE
            holder.kills!!.text = this.context.resources.getString(R.string.text_wia_killed_by) + " " + stat.kills + " " + this.context.resources.getString(R.string.text_wia_times)
            holder.headshots!!.visibility = View.GONE
            holder.vehiclekills!!.visibility = View.GONE
        }

        return convertView
    }

    internal class ViewHolder {
        var name: TextView? = null
        var weaponImage: NetworkImageView? = null
        var medal: ImageView? = null
        var kills: TextView? = null
        var ratios: TextView? = null
        var headshots: TextView? = null
        var vehiclekills: TextView? = null
    }
}
