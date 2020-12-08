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
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.appcore.dbg.DBGCensus
import com.cramsan.ps2link.appcore.dbg.content.Member
import java.util.ArrayList

class OnlineMemberItemAdapter(
    members: List<Member>,
    context: Context,
) : BaseAdapter() {
    protected var mInflater: LayoutInflater
    private val membersOnline: ArrayList<Member>

    init {
        this.mInflater = LayoutInflater.from(context)

        this.membersOnline = ArrayList()
        inf_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_inf)
        lia_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_lia)
        med_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_med)
        eng_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_eng)
        hea_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_hea)
        max_icon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_max)

        for (member in members) {
            if (member.online_status != "0") {
                this.membersOnline.add(member)
            }
        }
    }

    override fun getCount(): Int {
        return this.membersOnline.size
    }

    override fun getItem(position: Int): Member {
        return this.membersOnline[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_member_item, parent, false)

            holder = ViewHolder()
            holder.memberName =
                convertView!!.findViewById<View>(R.id.textViewMemberListName) as TextView
            holder.memberRank =
                convertView.findViewById<View>(R.id.textViewMemberListRank) as TextView
            holder.classIcon =
                convertView.findViewById<View>(R.id.imageViewMemberListClass) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        try {
            if (getItem(position).online_status == "0") {
                holder.memberName!!.setTextColor(Color.WHITE)
                holder.memberRank!!.setTextColor(Color.RED)
            } else {
                holder.memberName!!.setTextColor(Color.WHITE)
                holder.memberRank!!.setTextColor(Color.GREEN)
            }
            holder.memberName!!.text = getItem(position).character!!.name!!.first
            val currentClassId = getItem(position).character!!.profile_id

            if (currentClassId == "4" || currentClassId == "19" || currentClassId == "12") {
                holder.classIcon!!.setImageBitmap(lia_icon)
            } else if (currentClassId == "7" || currentClassId == "22" || currentClassId == "15") {
                holder.classIcon!!.setImageBitmap(hea_icon)
            } else if (currentClassId == "5" || currentClassId == "20" || currentClassId == "13") {
                holder.classIcon!!.setImageBitmap(med_icon)
            } else if (currentClassId == "6" || currentClassId == "21" || currentClassId == "14") {
                holder.classIcon!!.setImageBitmap(eng_icon)
            } else if (currentClassId == "8" || currentClassId == "23" || currentClassId == "16") {
                holder.classIcon!!.setImageBitmap(max_icon)
            } else if (currentClassId == "2" || currentClassId == "17" || currentClassId == "10") {
                holder.classIcon!!.setImageBitmap(inf_icon)
            } else {
                holder.classIcon!!.setImageBitmap(null)
            }
        } catch (e: NullPointerException) {
            holder.memberName!!.text = "UNKOWN"
            holder.memberRank!!.text = "UNKOWN"
            holder.memberName!!.setTextColor(Color.GRAY)
            holder.memberRank!!.setTextColor(Color.GRAY)
            holder.classIcon!!.setImageBitmap(null)
        }

        return convertView
    }

    internal class ViewHolder {
        var memberName: TextView? = null
        var memberRank: TextView? = null
        var classIcon: ImageView? = null
    }

    companion object {

        private lateinit var inf_icon: Bitmap
        private lateinit var lia_icon: Bitmap
        private lateinit var med_icon: Bitmap
        private lateinit var eng_icon: Bitmap
        private lateinit var hea_icon: Bitmap
        private lateinit var max_icon: Bitmap
    }
}
