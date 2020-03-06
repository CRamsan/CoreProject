package com.cesarandres.ps2link.dbg.view

import android.database.Cursor
import android.view.LayoutInflater
import android.widget.BaseAdapter

/**
 * This abstract class provides some extra functionality for adapters that need
 * to read from the database
 */
abstract class DBItemAdapter : BaseAdapter() {
    protected var mInflater: LayoutInflater? = null
    protected var cursor: Cursor? = null
    protected var size: Int = 0
}
