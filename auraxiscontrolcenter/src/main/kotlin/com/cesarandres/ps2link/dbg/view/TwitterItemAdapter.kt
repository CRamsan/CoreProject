package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.module.twitter.PS2Tweet
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

class TwitterItemAdapter(
    context: Context,
    users: Array<String>,
    data: ObjectDataSource,
    val imageLoader: ImageLoader
) :
    DBItemAdapter() {

    init {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context)
        this.size = data.countAllTweets(users)
        this.cursor = data.getTweetCursor(users)
    }

    override fun getCount(): Int {
        return this.size
    }

    override fun getItem(position: Int): PS2Tweet? {
        var tweet: PS2Tweet? = null
        try {
            tweet = ObjectDataSource.cursorToTweet(
                ObjectDataSource.cursorToPosition(
                    cursor!!,
                    position
                )
            )
        } catch (e: IllegalStateException) {
            tweet = PS2Tweet()
            tweet.id = ""
            tweet.date = 0
            tweet.content = ""
            tweet.user = ""
            tweet.tag = ""
            tweet.imgUrl = ""
        }

        return tweet
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater!!.inflate(R.layout.layout_tweet_item, parent, false)

            holder = ViewHolder()
            holder.tweetName =
                convertView!!.findViewById<View>(R.id.textViewTwitterName) as TextView
            holder.tweetTag = convertView.findViewById<View>(R.id.textViewTwitterTag) as TextView
            holder.tweetText = convertView.findViewById<View>(R.id.textViewTwitterText) as TextView
            holder.tweetDate = convertView.findViewById<View>(R.id.textViewTwitterDate) as TextView
            holder.userImage =
                convertView.findViewById<View>(R.id.networkImageViewTweet) as NetworkImageView
            holder.userImage!!.setErrorImageResId(R.drawable.image_not_found)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val tweet = getItem(position)
        holder.tweetName!!.text = tweet!!.user
        holder.tweetText!!.text = tweet.content
        Linkify.addLinks(holder.tweetText!!, Linkify.WEB_URLS)
        holder.tweetText!!.isFocusable = false
        holder.tweetTag!!.text = "@" + tweet.tag!!
        val updateTime = p.format(Date(tweet.date!! * 1000L))

        val newURL = tweet.imgUrl!!.replace("http:", "https:")
        holder.userImage!!.setImageUrl(newURL, imageLoader)
        holder.tweetDate!!.text = updateTime

        return convertView
    }

    internal class ViewHolder {
        var tweetName: TextView? = null
        var tweetTag: TextView? = null
        var tweetText: TextView? = null
        var tweetDate: TextView? = null
        var userImage: NetworkImageView? = null
    }

    companion object {

        private val p = PrettyTime()
    }
}
