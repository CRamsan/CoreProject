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
import com.cesarandres.ps2link.dbg.content.CharacterEvent
import com.cesarandres.ps2link.dbg.content.response.Characters_event_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier
import com.cesarandres.ps2link.dbg.view.KillItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity

/**
 * This fragment will retrieve the killboard of a player and display it.
 */
class FragmentKillList : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_kill_list, container, false)
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
        val listRoot = activity!!.findViewById<View>(R.id.listViewKillList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            mCallbacks!!.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as CharacterEvent).important_character_id,
                    this.namespace!!.name
                )
            )
        }

        this.profileId = arguments!!.getString("PARAM_0")
        this.namespace = DBGCensus.Namespace.valueOf(arguments!!.getString("PARAM_1"))
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadKillList(this.profileId)
    }

    /**
     * @param character_id retrieve the killboard for a character with the given
     * character_id and displays it.
     */
    fun downloadKillList(character_id: String?) {
        setProgressButton(true)
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET,
            PS2Collection.CHARACTERS_EVENT, null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                SearchModifier.EQUALS,
                character_id!!
            )
                .AddCommand(
                    QueryCommand.RESOLVE,
                    "character,attacker"
                ).AddCommand(QueryCommand.LIMIT, "100")
                .AddComparison("type", SearchModifier.EQUALS, "DEATH,KILL"),
            this.namespace!!
        )!!.toString()
        val success = Listener<Characters_event_list_response> { response ->
            setProgressButton(false)
            try {
                if (response.characters_event_list == null) {
                    return@Listener
                }
                val listRoot = activity!!.findViewById<View>(R.id.listViewKillList) as ListView
                listRoot.adapter =
                    KillItemAdapter(activity!!, response.characters_event_list!!,
                        this!!.profileId!!,
                        this.namespace!!,
                        volley,
                        imageLoader,
                        dbgCensus
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
        dbgCensus.sendGsonRequest(
            url,
            Characters_event_list_response::class.java,
            success,
            error,
            this
        )
    }

    companion object {
        const val TAG = "FragmentKillList"
    }
}
