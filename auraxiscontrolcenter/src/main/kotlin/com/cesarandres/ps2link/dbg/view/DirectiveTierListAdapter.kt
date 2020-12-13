package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.ProgressBar
import android.widget.TextView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.dbg.util.EmbeddableExpandableListView
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.DBGCensus
import com.cramsan.ps2link.appcore.dbg.content.Directive
import java.util.ArrayList

class DirectiveTierListAdapter(
    private val fragment: BasePS2Fragment,
    private val dbgCensus: DBGCensus
) : BaseExpandableListAdapter(),
    OnGroupExpandListener {
    private val expandableList: EmbeddableExpandableListView? = null
    private var directives: List<Directive>? = null

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

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.directives!![groupPosition]
    }

    override fun getGroupCount(): Int {
        try {
            return this.directives!!.size
        } catch (e: Exception) {
            this.directives = ArrayList()
            return this.directives!!.size
        }
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
        val headerTitle = getGroup(groupPosition) as Directive
        if (convertView == null) {
            val infalInflater = this.fragment.activity!!
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.layout_directive_item, parent, false)
        }

        val lblListHeader =
            convertView!!.findViewById<View>(R.id.textViewDirectiveCategoryName) as TextView
        val pbar = convertView.findViewById<View>(R.id.progressBarDirectiveProgress) as ProgressBar
        var name: String? = "None"
        try {
            name = headerTitle.name!!.localizedName(CensusLang.EN)
        } catch (e: Exception) {
        }

        lblListHeader.text = name
        var value = 0
        val max = 0
        try {
            value = Integer.parseInt(headerTitle.directive!!.directiveObjective!!.state_data!!)
            // max = Integer.parseInt(headerTitle.getDirective().getDirectiveObjective().getObjective_id_join_objective().getParam1());
        } catch (e: Exception) {
        }

        pbar.progress = value
        pbar.max = max
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
                this.expandableList!!.collapseGroup(i)
            }
        }
    }

    fun setDirectives(directives: List<Directive>) {
        this.directives = directives
    }
}
