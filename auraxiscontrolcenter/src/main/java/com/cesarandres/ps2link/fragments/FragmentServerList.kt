package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast

import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.World
import com.cesarandres.ps2link.dbg.content.WorldEvent
import com.cesarandres.ps2link.dbg.content.response.Server_Status_response
import com.cesarandres.ps2link.dbg.content.response.Server_response
import com.cesarandres.ps2link.dbg.content.response.World_event_list_response
import com.cesarandres.ps2link.dbg.content.response.server.PS2
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.view.ServerItemAdapter
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity


import java.util.ArrayList

/**
 * This fragment will display the servers and theirs status
 */
class FragmentServerList : BaseFragment(), SourceSelectionChangedListener {

    private var selectionButton: ButtonSelectSource? = null

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
        val view = inflater.inflate(R.layout.fragment_server_list, container, false)
        selectionButton = ButtonSelectSource(
            activity!!,
            activity!!.findViewById<View>(R.id.linearLayoutTitle) as ViewGroup,
            metrics,
            dbgCensus
        )
        selectionButton!!.listener = this
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val titleLayout = activity!!.findViewById<View>(R.id.linearLayoutTitle) as LinearLayout
        selectionButton!!.removeButtons(activity!!, titleLayout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.fragmentTitle.text = getString(R.string.title_servers)
        this.fragmentUpdate.setOnClickListener {
            metrics.log(TAG, "Update")
            downloadServers()
        }

        val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val listParent = activity!!.findViewById<View>(R.id.listViewServers) as ListView
            (listParent.adapter as ServerItemAdapter).onItemSelected(myItemInt, this!!.context!!)
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        this.fragmentUpdate.visibility = View.VISIBLE
        activityContainer.activityMode = ActivityMode.ACTIVITY_SERVER_LIST
        downloadServers()
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    fun downloadServers() {
        setProgressButton(true)
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET, PS2Collection.WORLD, "",
            QueryString.generateQeuryString().AddCommand(QueryCommand.LIMIT, "10"),
            selectionButton!!.namespace
        )!!.toString()

        val success = Listener<Server_response> { response ->
            try {
                val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
                listRoot.adapter = ServerItemAdapter(activity!!, response.world_list!!, dbgCensus, selectionButton!!.namespace)

                for (world in response.world_list!!) {
                    downloadServerAlert(world.world_id)
                }

            } catch (e: Exception) {
                metrics.log(TAG, "${Constants.ERROR_PARSING_RESPONE}-DownloadServers")
                eventLogger.log(Severity.ERROR, TAG, "${Constants.ERROR_PARSING_RESPONE}-DownloadServers")
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }

            setProgressButton(false)
            downloadServerPopulation()
        }

        val error = ErrorListener {
            setProgressButton(false)
            metrics.log(TAG, "${Constants.ERROR_MAKING_REQUEST}-DownloadServers")
            eventLogger.log(Severity.ERROR, TAG, "${Constants.ERROR_MAKING_REQUEST}-DownloadServers")
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }
        dbgCensus.sendGsonRequest(url, Server_response::class.java, success, error, this)
    }

    /**
     * Query the API and retrieve the latest population info for all the servers
     * and update the UI. This method uses a non-standard API call. This API
     * call has been unreliable in the past.
     */
    fun downloadServerPopulation() {
        setProgressButton(true)
        // This is not an standard API call
        val url = "https://census.daybreakgames.com/s:PS2Link/json/status/ps2"

        val success = Listener<Server_Status_response> { response ->
            setProgressButton(false)
            try {
                val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
                val servers = response.ps2
                (listRoot.adapter as ServerItemAdapter).setServerPopulation(servers!!)
            } catch (e: Exception) {
                metrics.log(TAG, "${Constants.ERROR_PARSING_RESPONE}-DownloadServerPopulation")
                eventLogger.log(Severity.ERROR, TAG, "${Constants.ERROR_PARSING_RESPONE}-DownloadServerPopulation")
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            metrics.log(TAG, "${Constants.ERROR_MAKING_REQUEST}-DownloadServerPopulation")
            eventLogger.log(Severity.ERROR, TAG, "${Constants.ERROR_MAKING_REQUEST}-DownloadServerPopulation")
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }

        dbgCensus.sendGsonRequest(url, Server_Status_response::class.java, success, error, this)
    }

    /**
     * This call will request the last METAGEME event less than 15 minutes old for this given server
     */
    private fun downloadServerAlert(serverId: String?) {
        setProgressButton(true)
        // The URL looks like this:
        // http://census.daybreakgames.com/get/ps2:v2/world_event?
        // world_id=17&c:limit=1&type=METAGAME&c:join=metagame_event&c:lang=en
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET, PS2Collection.WORLD_EVENT, "",
            QueryString.generateQeuryString().AddCommand(
                QueryCommand.LIMIT,
                "1"
            ).AddComparison(
                "type",
                QueryString.SearchModifier.EQUALS,
                "METAGAME"
            ).AddComparison("world_id", QueryString.SearchModifier.EQUALS, serverId!!).AddComparison(
                "after", QueryString.SearchModifier.EQUALS,
                //Get metagame events that are newer than  minutes
                java.lang.Long.toString(System.currentTimeMillis() / 1000L - 7200)
            ).AddCommand(QueryCommand.JOIN, "metagame_event"),
            selectionButton!!.namespace
        )!!.toString()

        val success = Listener<World_event_list_response> { response ->
            setProgressButton(false)
            try {
                val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
                val events = response.world_event_list
                if (events != null && events.size > 0) {
                    (listRoot.adapter as ServerItemAdapter).setServerAlert(events[0])
                }
            } catch (e: Exception) {
                metrics.log(TAG, "${Constants.ERROR_PARSING_RESPONE}-DownloadServerAlert")
                eventLogger.log(Severity.ERROR, TAG, "${Constants.ERROR_PARSING_RESPONE}-DownloadServerAlert")
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            metrics.log(TAG, "${Constants.ERROR_MAKING_REQUEST}-DownloadServerAlert")
            eventLogger.log(Severity.ERROR, TAG, "${Constants.ERROR_MAKING_REQUEST}-DownloadServerAlert")
            setProgressButton(false)
        }

        dbgCensus.sendGsonRequest(url, World_event_list_response::class.java, success, error, this)
    }

    override fun onSourceSelectionChanged(selectedNamespace: Namespace) {
        volley.cancelAll(this)
        downloadServers()
    }

    companion object {
        private const val TAG = "FragmentServerList"
    }
}