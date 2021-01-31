package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.network.models.content.CharacterFriend

class FriendItemAdapter(
    private val context: Context,
    _friends: List<CharacterFriend>
) : BaseAdapter() {

    protected var mInflater: LayoutInflater
    private val friends = _friends.toMutableList()

    init {
        this.mInflater = LayoutInflater.from(context)

        var i = 0
        while (i < friends.size) {
            if (!friends[i].isValid) {
                friends.removeAt(i)
                i--
            }
            i++
        }
        // TODO: Sort this list
        //  Collections.sort(this.friends)
    }

    override fun getCount(): Int {
        return this.friends.size
    }

    override fun getItem(position: Int): CharacterFriend {
        return friends[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_friend_item, parent, false)

            holder = ViewHolder()
            holder.friendName =
                convertView!!.findViewById<View>(R.id.textViewFriendName) as TextView
            holder.friendStatus =
                convertView.findViewById<View>(R.id.textViewFriendtatus) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.friendName!!.text = getItem(position).name!!.first

        if (getItem(position).online == 0) {
            holder.friendStatus!!.text = context.getText(R.string.text_offline)
            holder.friendStatus!!.setTextColor(Color.RED)
        } else {
            holder.friendStatus!!.text = context.getText(R.string.text_online)
            holder.friendStatus!!.setTextColor(Color.GREEN)
        }

        return convertView
    }

    internal class ViewHolder {
        var friendName: TextView? = null
        var friendStatus: TextView? = null
    }
}
