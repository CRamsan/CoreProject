package com.cesarandres.ps2link.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.dbg.volley.GsonRequest
import com.cesarandres.ps2link.module.reddit.Child
import com.cesarandres.ps2link.module.reddit.Content
import com.cesarandres.ps2link.module.reddit.RedditItemAdapter

/**
 * Fragment that retrieves the hottest Reddit post
 */
class FragmentReddit : BasePS2Fragment() {
    private var subReddit: String? = null

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reddit, container, false)
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
        this.fragmentTitle.text = getString(R.string.title_reddit)
        val listRoot = view!!.findViewById<View>(R.id.listViewRedditList) as ListView
        val bundle = arguments
        this.subReddit = bundle!!.getString("PARAM_0")
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val child = myAdapter.getItemAtPosition(myItemInt) as Child
            val openRedditIntent = Intent(Intent.ACTION_VIEW, Uri.parse(child.data!!.url))
            startActivity(openRedditIntent)
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        updatePosts()
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onPause()
     */
    override fun onPause() {
        super.onPause()
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroyView()
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * Send a request to get all the information from the Reddit server
     */
    fun updatePosts() {
        setProgressButton(true)
        val url = REDDIT_URL + this.subReddit + REDDIT_ENDPOINT

        val success = Listener<Content> { response ->
            setProgressButton(false)
            try {
                val listRoot = view!!.findViewById<View>(R.id.listViewRedditList) as ListView
                listRoot.adapter = RedditItemAdapter(this!!.activity!!, response.data!!.children, imageLoader)
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    view!!.resources.getString(R.string.toast_error_retrieving_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            Toast.makeText(
                activity,
                view!!.resources.getString(R.string.toast_error_retrieving_data),
                Toast.LENGTH_SHORT
            ).show()
        }

        val gsonOject = GsonRequest(url, Content::class.java, null, success, error)
        gsonOject.tag = this
        volley.add(gsonOject)
    }

    companion object {

        val REDDIT_URL = "https://www.reddit.com/r/"
        val REDDIT_ENDPOINT = "/hot.json"
    }
}
