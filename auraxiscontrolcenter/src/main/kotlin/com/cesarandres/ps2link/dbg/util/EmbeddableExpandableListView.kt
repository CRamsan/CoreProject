package com.cesarandres.ps2link.dbg.util

import android.content.Context
import android.widget.ExpandableListView

class EmbeddableExpandableListView(context: Context) : ExpandableListView(context) {

    var row_height = 20
    var rows: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, rows * row_height)
    }
}
