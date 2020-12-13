package com.cesarandres.ps2link.dbg.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.toolbox.ImageLoader
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.dbg.util.EmbeddableExpandableListView
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.DBGCensus
import com.cramsan.ps2link.appcore.dbg.content.DirectiveTreeCategory
import java.util.ArrayList

class DirectiveTreeCategoryListAdapter(
    private val fragment: BasePS2Fragment,
    private val expandableList: ExpandableListView,
    private val dbgCensus: DBGCensus,
    private val imageLoader: ImageLoader
) : BaseExpandableListAdapter(), OnGroupExpandListener {

    protected var mInflater: LayoutInflater
    private var categories: ArrayList<DirectiveTreeCategory>? = null
    private val nextAdapter: DirectiveTreeListAdapter
    private var nextList: EmbeddableExpandableListView? = null

    init {
        this.mInflater = LayoutInflater.from(fragment.activity)
        this.nextAdapter = DirectiveTreeListAdapter(fragment, imageLoader, dbgCensus)
    }

    override fun getChild(groupPosition: Int, childPosititon: Int): Any? {
        return null
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View,
        parent: ViewGroup
    ): View {

        this.nextAdapter.setDirectiveTrees(this.categories!![groupPosition].characterDirectiveTreeList!!)

        if (this.nextList == null) {
            this.nextList = EmbeddableExpandableListView(this.fragment.activity!!)
            this.nextList!!.rows =
                this.categories!![groupPosition].characterDirectiveTreeList!!.size
            this.nextList!!.row_height = 150
            this.nextList!!.setAdapter(nextAdapter)
        }
        this.nextAdapter.notifyDataSetInvalidated()
        return this.nextList!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.categories!![groupPosition]
    }

    override fun getGroupCount(): Int {
        return this.categories!!.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_directive_category_item, parent, false)

            holder = ViewHolder()
            holder.categoryName =
                convertView!!.findViewById<View>(R.id.textViewDirectiveCategoryName) as TextView
            holder.progress =
                convertView.findViewById<View>(R.id.progressBarDirectiveCategoryValue) as ProgressBar
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val category = this.categories!![groupPosition]
        holder.categoryName!!.text = category.name!!.localizedName(CensusLang.EN)
        holder.progress!!.max = category.maxValue
        holder.progress!!.progress = category.currentValue

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun onGroupExpand(groupPosition: Int) {
        val len = groupCount

        for (i in 0 until len) {
            if (i != groupPosition) {
                this.expandableList.collapseGroup(i)
            }
        }
    }

    fun setCategories(categories: ArrayList<DirectiveTreeCategory>) {
        this.categories = categories
    }

    internal class ViewHolder {
        var categoryName: TextView? = null
        var progress: ProgressBar? = null
    }
}
