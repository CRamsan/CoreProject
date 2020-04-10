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
import com.cesarandres.ps2link.dbg.content.Member
import com.cesarandres.ps2link.dbg.content.response.Outfit_member_response
import com.cesarandres.ps2link.dbg.view.OnlineMemberItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity
import java.util.ArrayList

/**
 * This fragment will do a request to retrieve all members for the given outfit
 * and resolve the class they are using. This is very useful to show who is
 * online and display their current class
 */
class FragmentMembersOnline : BaseFragment() {

    private var outfitId: String? = null
    private val outfitName: String? = null
    private var namespace: DBGCensus.Namespace? = null

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
        return inflater.inflate(R.layout.fragment_member_list, container, false)
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

        if (savedInstanceState == null) {
            this.outfitId = arguments!!.getString("PARAM_0")
        } else {
            this.outfitId = savedInstanceState.getString("outfitId")
        }
        this.namespace = DBGCensus.Namespace.valueOf(arguments!!.getString("PARAM_1", ""))

        this.fragmentTitle.text = outfitName
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadOutfitMembers()
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("outfitId", outfitId)
    }

    /**
     * Do a request to retrieve all the members of the given outfit with their
     * classes already resolved
     */
    fun downloadOutfitMembers() {
        setProgressButton(true)
        val url =
            dbgCensus.generateGameDataRequest("outfit_member?c:limit=10000&c:resolve=online_status,character(name,battle_rank,profile_id)&c:join=type:profile^list:0^inject_at:profile^show:name." + dbgCensus.currentLang.name.toLowerCase() + "^on:character.profile_id^to:profile_id&outfit_id=" + this.outfitId, namespace!!)!!.toString()

        val success = Listener<Outfit_member_response> { response ->
            setProgressButton(false)
            try {
                updateContent(response.outfit_member_list)
            } catch (e: Exception) {
                eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_PARSING_RESPONE)
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_MAKING_REQUEST)
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }

        dbgCensus.sendGsonRequest(url, Outfit_member_response::class.java, success, error, this)
    }

    /**
     * @param members An array list with all the members found. The adapter will
     * retrieve all online members and it will only display those
     */
    private fun updateContent(members: ArrayList<Member>?) {
        val listRoot = view!!.findViewById<View>(R.id.listViewMemberList) as ListView

        listRoot.adapter = OnlineMemberItemAdapter(members!!, activity!!, dbgCensus)
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            mCallbacks!!.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as Member).character_id,
                    this.namespace!!.name
                )
            )
        }
    }

    companion object {
        const val TAG = "FragmentMembersOnline"
    }
}
