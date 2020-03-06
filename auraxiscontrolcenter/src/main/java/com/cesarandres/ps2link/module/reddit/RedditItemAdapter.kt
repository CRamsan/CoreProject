package com.cesarandres.ps2link.module.reddit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.android.volley.toolbox.NetworkImageView
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R

import org.ocpsoft.prettytime.PrettyTime

import java.util.Date

class RedditItemAdapter(private val context: Context, private val children: List<Child>) :
    BaseAdapter() {
    protected var mInflater: LayoutInflater

    init {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return this.children.size
    }

    override fun getItem(position: Int): Child {
        return this.children[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_reddit_item, parent, false)

            holder = ViewHolder()
            holder.author = convertView!!.findViewById<View>(R.id.textViewRedditAuthor) as TextView
            holder.comments =
                convertView.findViewById<View>(R.id.textViewRedditComments) as TextView
            holder.date = convertView.findViewById<View>(R.id.textViewRedditCreated) as TextView
            holder.flare = convertView.findViewById<View>(R.id.textViewRedditFlare) as TextView
            holder.domain = convertView.findViewById<View>(R.id.textViewRedditDomain) as TextView
            holder.score = convertView.findViewById<View>(R.id.textViewRedditScore) as TextView
            holder.title = convertView.findViewById<View>(R.id.textViewRedditTitle) as TextView
            holder.thumbnail =
                convertView.findViewById<View>(R.id.networkImageViewRedditThumbnail) as NetworkImageView
            holder.thumbnail!!.setErrorImageResId(R.drawable.image_not_found)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val child = getItem(position)

        holder.author!!.text = child.data!!.author
        holder.comments!!.text =
            Integer.toString(child.data!!.numComments!!) + " " + context.resources.getString(R.string.text_comments)
        val updateTime = prettyTime.format(Date(child.data!!.createdUtc!! * 1000L))
        holder.date!!.text = updateTime
        holder.domain!!.text = child.data!!.domain
        if (child.data!!.linkFlairText != null) {
            holder.flare!!.visibility = View.VISIBLE
            holder.flare!!.text = child.data!!.linkFlairText
        } else {
            holder.flare!!.visibility = View.GONE
            holder.flare!!.text = child.data!!.linkFlairText
        }
        holder.score!!.text = Integer.toString(child.data!!.score!!)
        holder.title!!.text = child.data!!.title

        holder.thumbnail!!.setDefaultImageResId(0)
        holder.thumbnail!!.setImageUrl("", null)
        if (child.data!!.thumbnail != "self") {
            holder.thumbnail!!.setImageUrl(child.data!!.thumbnail, ApplicationPS2Link.mImageLoader)
        } else {
            holder.thumbnail!!.setDefaultImageResId(R.drawable.image_not_found)
            holder.thumbnail!!.setImageUrl("", null)
        }

        return convertView
    }

    internal class ViewHolder {
        var thumbnail: NetworkImageView? = null
        var score: TextView? = null
        var flare: TextView? = null
        var title: TextView? = null
        var author: TextView? = null
        var domain: TextView? = null
        var date: TextView? = null
        var comments: TextView? = null
    }

    companion object {

        private val prettyTime = PrettyTime()
    }
}