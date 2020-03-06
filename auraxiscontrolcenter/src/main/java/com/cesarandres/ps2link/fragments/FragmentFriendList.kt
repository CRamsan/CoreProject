package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast

import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.CharacterFriend
import com.cesarandres.ps2link.dbg.content.response.Character_friend_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier
import com.cesarandres.ps2link.dbg.view.FriendItemAdapter

/**
 * This fragment will display the friends of a given user. This fragment is
 * designed to be part of a profile pager.
 */
class FragmentFriendList : BaseFragment() {

    private var profileId: String? = null

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
            mCallbacks.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as CharacterFriend).character_id,
                    DBGCensus.currentNamespace.name
                )
            )
        }

        this.profileId = arguments!!.getString("PARAM_0")

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
        val url = DBGCensus.generateGameDataRequest(
            Verb.GET,
            PS2Collection.CHARACTERS_FRIEND,
            null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                SearchModifier.EQUALS,
                character_id!!
            )
                .AddCommand(QueryCommand.RESOLVE, "character_name")
        )!!.toString()

        val success = Listener<Character_friend_list_response> { response ->
            setProgressButton(false)
            try {
                val listRoot = activity!!.findViewById<View>(R.id.listViewFriendList) as ListView
                listRoot.adapter =
                    FriendItemAdapter(activity!!, response.characters_friend_list!![0].friend_list!!)
            } catch (e: Exception) {
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }

        DBGCensus.sendGsonRequest(
            url,
            Character_friend_list_response::class.java,
            success,
            error,
            this
        )
    }
}