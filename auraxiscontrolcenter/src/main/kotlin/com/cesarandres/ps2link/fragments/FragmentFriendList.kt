package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cramsan.ps2link.appcore.dbg.Verb
import com.cramsan.ps2link.appcore.dbg.content.CharacterFriend
import com.cramsan.ps2link.appcore.dbg.content.response.Character_friend_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier
import com.cesarandres.ps2link.dbg.view.FriendItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will display the friends of a given user. This fragment is
 * designed to be part of a profile pager.
 */
class FragmentFriendList : BaseFragment() {

    private var profileId: String? = null
    private var namespace: Namespace? = null

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
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
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
        val listRoot = activity!!.findViewById<View>(R.id.listViewFriendList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            mCallbacks!!.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as CharacterFriend).character_id,
                    namespace!!.name
                )
            )
        }

        this.profileId = arguments!!.getString("PARAM_0")
        this.namespace = Namespace.valueOf(arguments!!.getString("PARAM_1", ""))
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadFriendsList(this.profileId)
    }

    /**
     * @param character_id Character id that will be used to request the list of friends
     */
    fun downloadFriendsList(character_id: String?) {
        setProgressButton(true)
        viewLifecycleOwner.lifecycleScope.launch {
            val friendList = withContext(Dispatchers.IO) { dbgCensus.getFriendList(character_id!!, namespace!!, CensusLang.EN) }
            setProgressButton(false)
            val listRoot = activity!!.findViewById<View>(R.id.listViewFriendList) as ListView
            listRoot.adapter =
                FriendItemAdapter(activity!!, friendList!!)
        }
    }

    companion object {
        const val TAG = "FragmentFriendList"
    }
}
