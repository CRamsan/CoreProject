package com.cesarandres.ps2link.dbg.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.content.CharacterDirectiveTree
import com.cesarandres.ps2link.dbg.util.EmbeddableExpandableListView
import java.util.ArrayList

class DirectiveTreeListAdapter(
    private val fragment: BaseFragment,
    private val imageLoader: ImageLoader,
    private val dbgCensus: DBGCensus
) : BaseExpandableListAdapter(),
    OnGroupExpandListener {

    protected var mInflater: LayoutInflater
    private val expandableList: EmbeddableExpandableListView? = null
    private var directiveTrees: ArrayList<CharacterDirectiveTree>? = null
    private val nextAdapter: DirectiveTierListAdapter
    private var nextList: EmbeddableExpandableListView? = null

    init {
        this.nextAdapter = DirectiveTierListAdapter(fragment, dbgCensus)
        this.mInflater = LayoutInflater.from(fragment.activity)
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

        this.nextAdapter.setDirectives(this.directiveTrees!![groupPosition].directive_tier!!.directives!!)

        if (this.nextList == null) {
            this.nextList = EmbeddableExpandableListView(this.fragment.activity!!)
            this.nextList!!.rows =
                this.directiveTrees!![groupPosition].directive_tier!!.directives!!.size
            this.nextList!!.row_height = 75
            this.nextList!!.setAdapter(nextAdapter)
        }
        this.nextAdapter.notifyDataSetInvalidated()
        return this.nextList!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.directiveTrees!![groupPosition]
    }

    override fun getGroupCount(): Int {
        return this.directiveTrees!!.size
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
            convertView = mInflater.inflate(R.layout.layout_directive_tree_item, parent, false)

            holder = ViewHolder()
            holder.treeIcon =
                convertView!!.findViewById<View>(R.id.networkImageViewDirectoryTreeTier) as NetworkImageView
            holder.treeName =
                convertView.findViewById<View>(R.id.textViewDirectiveTreeName) as TextView
            holder.treeValue =
                convertView.findViewById<View>(R.id.textViewDirectiveTreeValueq) as TextView
            holder.treeLevel =
                convertView.findViewById<View>(R.id.imageViewDirectiveLevel) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val tree = this.directiveTrees!![groupPosition]
        holder.treeIcon!!.setImageUrl(
            DBGCensus.ENDPOINT_URL + "/" + tree.directive_tier!!.imagePath,
            imageLoader
        )
        holder.treeName!!.text = tree.directive_tree_id_join_directive_tree!!.name!!.localizedName(dbgCensus.currentLang)
        holder.treeValue!!.text = Integer.toString(tree.current_level_value)
        val resID = this.fragment.activity!!.resources.getIdentifier(
            "objective_progress_" + tree.current_directive_tier_id +
                "_0",
            "drawable", "com.cesarandres.ps2link"
        )
        holder.treeLevel!!.setImageResource(resID)
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

    fun setDirectiveTrees(directiveTrees: ArrayList<CharacterDirectiveTree>) {
        this.directiveTrees = directiveTrees
    }

    internal class ViewHolder {
        var treeIcon: NetworkImageView? = null
        var treeName: TextView? = null
        var treeValue: TextView? = null
        var treeLevel: ImageView? = null
    }
}
