package com.cesarandres.ps2link.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.CheckBox
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.LinearLayout
import android.widget.ListView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentTwitterBinding
import com.cesarandres.ps2link.dbg.view.TwitterItemAdapter
import com.cesarandres.ps2link.module.twitter.PS2Tweet
import com.cramsan.framework.core.NoopViewModel
import java.util.ArrayList
import java.util.Arrays

/**
 * Fragment that retrieves the Twitter feed for several users planetside 2
 * related. It also has UI to show and hide some users.
 */
class FragmentTwitter : BasePS2Fragment<NoopViewModel, FragmentTwitterBinding>() {

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
        super.onCreateView(inflater, container, savedInstanceState)

        val holder = view?.findViewById<View>(R.id.linearLayoutTwitterHolder) as LinearLayout
        this.users = requireActivity().resources.getStringArray(R.array.twitter_users)
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

        val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)

        val updateTweetLitener = OnCheckedChangeListener { buttonView, isChecked -> updateTweets() }

        for (user in this.users!!) {
            val userCheckbox = requireView().findViewWithTag<View>(user) as CheckBox
            userCheckbox.isChecked = settings.getBoolean(USER_PREFIX + user, false)
            userCheckbox.setOnCheckedChangeListener(updateTweetLitener)
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
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
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
        val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)
        val editor = settings.edit()
        for (user in this@FragmentTwitter.users!!) {
            val userCheckbox = requireView().findViewWithTag<View>(user) as CheckBox
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
            val userCheckbox = requireView().findViewWithTag<View>(user) as CheckBox
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
        val listRoot = requireActivity().findViewById<View>(R.id.listViewTweetList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val url =
                (
                    "https://twitter.com/#!/" + (myAdapter.getItemAtPosition(myItemInt) as PS2Tweet).tag + "/status/" +
                        (myAdapter.getItemAtPosition(myItemInt) as PS2Tweet).id
                    )
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        listRoot.adapter = TwitterItemAdapter(requireActivity(), users)
    }

    companion object {
        const val TAG = "FragmentTwitter"
    }

    override val logTag = "FragmentTwitter"
    override val contentViewLayout = R.layout.fragment_twitter
}
