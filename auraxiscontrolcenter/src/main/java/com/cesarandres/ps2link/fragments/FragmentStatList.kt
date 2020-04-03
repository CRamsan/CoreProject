package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.response.Character_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.view.StatItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity

/**
 * Retrieve the stats for the given character
 */
class FragmentStatList : BaseFragment() {

    private var profileId: String? = null
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
        return inflater.inflate(R.layout.fragment_stat_list, container, false)
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
        this.profileId = arguments!!.getString("PARAM_0")
        this.namespace = DBGCensus.Namespace.valueOf(arguments!!.getString("PARAM_1", ""))
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadStatList(this.profileId)
    }

    /**
     * @param character_id Character id of the character whose stats will be downloaded
     */
    fun downloadStatList(character_id: String?) {
        setProgressButton(true)
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET,
            PS2Collection.CHARACTER,
            character_id,
            QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "stat_history")
                .AddCommand(QueryCommand.HIDE, "name,battle_rank,certs,times,daily_ribbon"),
            this.namespace!!
        )!!.toString()

        val success = Listener<Character_list_response> { response ->
            setProgressButton(false)
            try {
                val listRoot = activity!!.findViewById<View>(R.id.listViewStatList) as ListView
                val profile = response.character_list!![0]
                val stats = profile.stats
                listRoot.adapter = StatItemAdapter(activity!!, stats!!.stat_history!!,
                    this!!.profileId!!
                )
            } catch (e: Exception) {
                metrics.log(TAG, Constants.ERROR_PARSING_RESPONE)
                eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_PARSING_RESPONE)
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            metrics.log(TAG, Constants.ERROR_MAKING_REQUEST)
            eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_MAKING_REQUEST)
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }

        dbgCensus.sendGsonRequest(url, Character_list_response::class.java, success, error, this)
    }

    companion object {
        const val TAG = "FragmentStatList"
    }
}
