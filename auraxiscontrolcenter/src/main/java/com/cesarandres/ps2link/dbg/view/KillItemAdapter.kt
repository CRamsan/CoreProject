package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.RequestQueue

import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.CharacterEvent
import com.cesarandres.ps2link.dbg.content.Faction
import com.cesarandres.ps2link.dbg.content.item.IContainDrawable
import com.cesarandres.ps2link.dbg.content.response.Item_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.volley.GsonRequest

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Hashtable
import java.util.Locale

class KillItemAdapter(
    private val context: Context,
    private val events: ArrayList<CharacterEvent>,
    private val characterId: String,
    private val volley: RequestQueue,
    private val imageLoader: ImageLoader,
    private val dbgCensus: DBGCensus
) : BaseAdapter() {

    protected var mInflater: LayoutInflater
    private val icon_vs: Bitmap
    private val icon_nc: Bitmap
    private val icon_tr: Bitmap

    private val requestTable: Hashtable<View, GsonRequest<Item_list_response>>

    init {
        this.mInflater = LayoutInflater.from(context)
        requestTable = Hashtable(20)
        icon_vs = BitmapFactory.decodeResource(context.resources, R.drawable.icon_faction_vs)
        icon_tr = BitmapFactory.decodeResource(context.resources, R.drawable.icon_faction_tr)
        icon_nc = BitmapFactory.decodeResource(context.resources, R.drawable.icon_faction_nc)
    }

    override fun getCount(): Int {
        return this.events.size
    }

    override fun getItem(position: Int): CharacterEvent {
        return this.events[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_kill_item, parent, false)

            holder = ViewHolder()
            holder.action =
                convertView!!.findViewById<View>(R.id.textViewKillItemAction) as TextView
            holder.faction =
                convertView.findViewById<View>(R.id.imageViewKillItemFactionIcon) as ImageView
            holder.name =
                convertView.findViewById<View>(R.id.TextViewKillItemCharacterName) as TextView
            holder.time = convertView.findViewById<View>(R.id.TextViewKillItemTime) as TextView
            holder.weaponName =
                convertView.findViewById<View>(R.id.TextViewKillItemWeaponName) as TextView
            holder.weaponImage =
                convertView.findViewById<View>(R.id.ImageViewKillItemWeaponImage) as NetworkImageView
            holder.weaponImage!!.setErrorImageResId(R.drawable.image_not_found)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            val request = requestTable[convertView]
            request?.cancel()
        }

        if (getItem(position).weapon_name != null) {
            holder.weaponName!!.text = getItem(position).weapon_name
            holder.weaponImage!!.setImageUrl(
                getItem(position).imagePath,
                imageLoader
            )
        } else {
            holder.weaponName!!.text = context.getText(R.string.text_loading_ellipsis)

            val event = getItem(position)
            val weapongId = event.attacker_weapon_id
            if (weapongId != "0") {
                holder.weaponImage!!.setDefaultImageResId(0)
                holder.weaponImage!!.setImageUrl("", null)
                downloadPictures(
                    getItem(position).attacker_weapon_id,
                    PS2Collection.ITEM,
                    holder.weaponName,
                    holder.weaponImage,
                    position,
                    convertView
                )
            } else if (getItem(position).attacker_vehicle_id != "0") {
                holder.weaponImage!!.setDefaultImageResId(0)
                holder.weaponImage!!.setImageUrl("", null)
                downloadPictures(
                    getItem(position).attacker_vehicle_id,
                    PS2Collection.VEHICLE,
                    holder.weaponName,
                    holder.weaponImage,
                    position,
                    convertView
                )
            } else {
                holder.weaponName!!.text = context.getText(R.string.text_unknown)
                holder.weaponImage!!.setDefaultImageResId(R.drawable.image_not_found)
                holder.weaponImage!!.setImageUrl("", null)
            }
        }

        val date = Date(java.lang.Long.parseLong(getItem(position).timestamp!! + "000"))
        val format = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())
        holder.time!!.text = format.format(date)

        try {
            if (getItem(position).attacker!!.character_Id == this.characterId) {
                holder.name!!.text = getItem(position).character!!.name!!.first
                getItem(position).important_character_id = getItem(position).character_id
                if (getItem(position).character_id == this.characterId) {
                    holder.action!!.text = context.getText(R.string.text_suicide_caps)
                    holder.action!!.setTextColor(Color.RED)
                } else {
                    holder.action!!.text = context.getText(R.string.text_killed_caps)
                    holder.action!!.setTextColor(Color.GREEN)
                }
                if (getItem(position).character!!.faction_id == Faction.VS) {
                    holder.faction!!.setImageBitmap(icon_vs)
                } else if (getItem(position).character!!.faction_id == Faction.NC) {
                    holder.faction!!.setImageBitmap(icon_nc)
                } else if (getItem(position).character!!.faction_id == Faction.TR) {
                    holder.faction!!.setImageBitmap(icon_tr)
                }
            } else if (getItem(position).character_id == this.characterId) {
                holder.action!!.text = context.getText(R.string.text_killed_by_caps)
                holder.action!!.setTextColor(Color.RED)
                holder.name!!.text = getItem(position).attacker!!.name!!.first
                getItem(position).important_character_id = getItem(position).attacker_character_id
                if (getItem(position).attacker!!.faction_id == Faction.VS) {
                    holder.faction!!.setImageBitmap(icon_vs)
                } else if (getItem(position).attacker!!.faction_id == Faction.NC) {
                    holder.faction!!.setImageBitmap(icon_nc)
                } else if (getItem(position).attacker!!.faction_id == Faction.TR) {
                    holder.faction!!.setImageBitmap(icon_tr)
                }
            }
        } catch (e: NullPointerException) {

        }

        return convertView
    }

    fun downloadPictures(
        resource_id: String?,
        collection: PS2Collection,
        name: TextView?,
        image: NetworkImageView?,
        position: Int,
        view: View
    ) {
        val url =
            dbgCensus.generateGameDataRequest(Verb.GET, collection, resource_id, null)!!.toString()
        val success = Listener<Item_list_response> { response ->
            var item: IContainDrawable? = null

            if (response.item_list != null && response.item_list!!.size > 0) {
                item = response.item_list!![0]
            } else if (response.vehicle_list != null && response.vehicle_list!!.size > 0) {
                item = response.vehicle_list!![0]
            }

            if (item != null) {
                var weaponName: String? = item.nameText
                if (weaponName == null) {
                    weaponName = context.getText(R.string.text_unknown).toString()
                }
                events[position].weapon_name = weaponName
                name!!.text = weaponName

                events[position].imagePath = DBGCensus.ENDPOINT_URL + "/" + item.imagePath
                image!!.setImageUrl(
                    DBGCensus.ENDPOINT_URL + "/" + item.imagePath,
                    imageLoader
                )
            } else {
                events[position].weapon_name = context.getText(R.string.text_unknown).toString()
                name!!.text = context.getText(R.string.text_unknown).toString()
                image!!.setImageUrl("", null)
            }
        }

        val error = ErrorListener {
            name!!.text = context.getText(R.string.text_unknown)
            image!!.setDefaultImageResId(R.drawable.image_not_found)
            image.setImageUrl("", null)
        }

        val request = GsonRequest(url, Item_list_response::class.java, null, success, error)
        requestTable[view] = request
        volley.add(request)
    }

    internal class ViewHolder {
        var action: TextView? = null
        var faction: ImageView? = null
        var name: TextView? = null
        var weaponName: TextView? = null
        var weaponImage: NetworkImageView? = null
        var time: TextView? = null
    }
}