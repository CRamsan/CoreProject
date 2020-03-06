package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.cesarandres.ps2link.R

class LoadingItemAdapter(context: Context) : BaseAdapter() {
    private val mInflater: LayoutInflater

    init {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_loading_item, parent, false)
        }

        return convertView!!
    }
}
