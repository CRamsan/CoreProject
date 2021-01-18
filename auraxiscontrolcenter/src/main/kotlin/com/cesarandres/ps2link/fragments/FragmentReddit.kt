package com.cesarandres.ps2link.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentRedditBinding
import com.cesarandres.ps2link.dbg.volley.GsonRequest
import com.cesarandres.ps2link.module.reddit.Child
import com.cesarandres.ps2link.module.reddit.Content
import com.cesarandres.ps2link.module.reddit.RedditItemAdapter
import com.cramsan.framework.core.NoopViewModel

/**
 * Fragment that retrieves the hottest Reddit post
 */
class FragmentReddit : BasePS2Fragment<NoopViewModel, FragmentRedditBinding>() {
    override val viewModel: NoopViewModel by viewModels()
    private var subReddit: String? = null

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
        val listRoot = requireView().findViewById<View>(R.id.listViewRedditList) as ListView
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

        val url = REDDIT_URL + this.subReddit + REDDIT_ENDPOINT

        val success = Listener<Content> { response ->

            try {
                val listRoot = requireView().findViewById<View>(R.id.listViewRedditList) as ListView
                listRoot.adapter = RedditItemAdapter(this!!.requireActivity(), response.data!!.children)
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    requireView().resources.getString(R.string.toast_error_retrieving_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val error = ErrorListener {

            Toast.makeText(
                activity,
                requireView().resources.getString(R.string.toast_error_retrieving_data),
                Toast.LENGTH_SHORT
            ).show()
        }

        val gsonOject = GsonRequest(url, Content::class.java, null, success, error)
        gsonOject.tag = this
    }

    companion object {

        val REDDIT_URL = "https://www.reddit.com/r/"
        val REDDIT_ENDPOINT = "/hot.json"
    }

    override val logTag = "FragmentReddit"
    override val contentViewLayout: Int
        get() = TODO("Not yet implemented")
}
