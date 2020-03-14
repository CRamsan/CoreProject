package com.cesarandres.ps2link.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.LinearLayout
import android.widget.ListView

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.view.TwitterItemAdapter
import com.cesarandres.ps2link.module.twitter.PS2Tweet
import com.cesarandres.ps2link.module.twitter.TwitterUtil
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag

import java.util.ArrayList
import java.util.Arrays

import twitter4j.TwitterException

/**
 * Fragment that retrieves the Twitter feed for several users planetside 2
 * related. It also has UI to show and hide some users.
 */
class FragmentTwitter : BaseFragment() {

    private val USER_PREFIX = "cb_"
    private var loaded = false
    private var users: Array<String>? = null

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_twitter, container, false)
        val holder = view.findViewById<View>(R.id.linearLayoutTwitterHolder) as LinearLayout
        this.users = activity!!.resources.getStringArray(R.array.twitter_users)
        for (user in this.users!!) {
            val cb = CheckBox(activity)
            cb.text = "@$user"
            cb.tag = user
            holder.addView(cb)
        }
        return view
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.fragmentTitle.text = getString(R.string.title_twitter)

        val settings = activity!!.getSharedPreferences("PREFERENCES", 0)

        val updateTweetLitener = OnCheckedChangeListener { buttonView, isChecked -> updateTweets() }

        for (user in this.users!!) {
            val userCheckbox = view!!.findViewWithTag<View>(user) as CheckBox
            userCheckbox.isChecked = settings.getBoolean(USER_PREFIX + user, false)
            userCheckbox.setOnCheckedChangeListener(updateTweetLitener)
        }

        this.fragmentUpdate.setOnClickListener {
            val usersnames = ArrayList<String>()

            for (user in this@FragmentTwitter.users!!) {
                val userCheckbox = view!!.findViewWithTag<View>(user) as CheckBox
                if (userCheckbox.isChecked) {
                    usersnames.add(user)
                }
            }

            val stringArray = Arrays.copyOf<String, Any>(
                usersnames.toTypedArray(),
                usersnames.size,
                Array<String>::class.java
            )

            val task = UpdateTweetsTask()
            setCurrentTask(task)
            task.execute(*stringArray)
        }

        val usersnames = ArrayList<String>()
        for (user in this@FragmentTwitter.users!!) {
            usersnames.add(user)
        }
        val stringArray = Arrays.copyOf<String, Any>(
            usersnames.toTypedArray(),
            usersnames.size,
            Array<String>::class.java
        )

        if (savedInstanceState == null) {
            val task = UpdateTweetsTask()
            setCurrentTask(task)
            task.execute(*stringArray)
        } else {
            this.loaded = savedInstanceState.getBoolean("twitterLoader", false)
            if (!this.loaded) {
                val task = UpdateTweetsTask()
                setCurrentTask(task)
                task.execute(*stringArray)
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        activityContainer.activityMode = ActivityMode.ACTIVITY_TWITTER
        this.fragmentUpdate.visibility = View.VISIBLE
        updateTweets()
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean("twitterLoader", loaded)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroyView()
     */
    override fun onDestroyView() {
        super.onDestroyView()
        val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
        val editor = settings.edit()
        for (user in this@FragmentTwitter.users!!) {
            val userCheckbox = view!!.findViewWithTag<View>(user) as CheckBox
            editor.putBoolean(USER_PREFIX + user, userCheckbox.isChecked)
        }
        editor.commit()
    }

    /**
     * Check the UI and update the content based on the selected users
     */
    private fun updateTweets() {
        val usersnames = ArrayList<String>()

        for (user in this@FragmentTwitter.users!!) {
            val userCheckbox = view!!.findViewWithTag<View>(user) as CheckBox
            if (userCheckbox.isChecked) {
                usersnames.add(user)
            }
        }

        val stringArray = Arrays.copyOf<String, Any>(
            usersnames.toTypedArray(),
            usersnames.size,
            Array<String>::class.java
        )
        updateContent(stringArray)
    }

    /**
     * @param users list of users to read from the database
     */
    private fun updateContent(users: Array<String>) {
        val listRoot = activity!!.findViewById<View>(R.id.listViewTweetList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val url =
                ("https://twitter.com/#!/" + (myAdapter.getItemAtPosition(myItemInt) as PS2Tweet).tag + "/status/"
                        + (myAdapter.getItemAtPosition(myItemInt) as PS2Tweet).id)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        val data = activityContainer.data
        listRoot.adapter = TwitterItemAdapter(activity!!, users, data!!, imageLoader)
    }

    /**
     * Task that will update the tweets for the given users. The tweets will be
     * cached into the database
     */
    private inner class UpdateTweetsTask : AsyncTask<String, Int, Array<String>>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            setProgressButton(true)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg users: String): Array<String> {
            var tweetList = ArrayList<PS2Tweet>(0)
            val data = activityContainer.data
            val currentTime = (System.currentTimeMillis() / 1000).toInt()
            val weekInSeconds = 60 * 60 * 24 * 7
            data!!.deleteAllTweet(currentTime - weekInSeconds)
            for (user in users) {
                if (this.isCancelled) {
                    break
                }
                try {
                    tweetList = TwitterUtil.getTweets(user)
                    data.insertAllTweets(tweetList, user)
                } catch (e: TwitterException) {
                    eventLogger.log(Severity.WARNING, classTag(), "Error trying retrieve tweets")
                } catch (e: IllegalStateException) {
                    eventLogger.log(Severity.ERROR, classTag(), "DB was closed. This is normal.")
                    break
                }

            }
            return users as Array<String>
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Array<String>?) {
            if (result != null) {
                updateTweets()
            }
            setProgressButton(false)
            loaded = true
        }
    }
}
