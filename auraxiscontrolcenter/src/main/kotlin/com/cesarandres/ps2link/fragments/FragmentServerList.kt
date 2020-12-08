package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.Verb
import com.cramsan.ps2link.appcore.dbg.content.response.Server_Status_response
import com.cramsan.ps2link.appcore.dbg.content.response.Server_response
import com.cramsan.ps2link.appcore.dbg.content.response.World_event_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.view.ServerItemAdapter
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity
import com.cramsan.ps2link.appcore.dbg.CensusLang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        lifecycleScope.launch {
            val serverList = withContext(Dispatchers.IO) { dbgCensus.getServerList(selectionButton!!.namespace, CensusLang.EN) }
            val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
            listRoot.adapter = ServerItemAdapter(activity!!, serverList!!, dbgCensus, selectionButton!!.namespace)
            for (world in serverList) {
                downloadServerAlert(world.world_id)
            }
            setProgressButton(false)
            downloadServerPopulation()
            idlingResource.decrement()
        }
    }

    /**
     * Query the API and retrieve the latest population info for all the servers
     * and update the UI. This method uses a non-standard API call. This API
     * call has been unreliable in the past.
     */
    fun downloadServerPopulation() {
        setProgressButton(true)
        lifecycleScope.launch {
            val serverPopulation = withContext(Dispatchers.IO) { dbgCensus.getServerPopulation() }
            setProgressButton(false)
            val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
            val servers = serverPopulation
            (listRoot.adapter as ServerItemAdapter).setServerPopulation(servers!!)
            idlingResource.decrement()
        }
    }

    /**
     * This call will request the last METAGEME event less than 15 minutes old for this given server
     */
    private fun downloadServerAlert(serverId: String?) {
        setProgressButton(true)
        lifecycleScope.launch {
            val serverMetadata = withContext(Dispatchers.IO) { dbgCensus.getServerMetadata(serverId!!, selectionButton!!.namespace, CensusLang.EN) }
            setProgressButton(false)
            val listRoot = activity!!.findViewById<View>(R.id.listViewServers) as ListView
            val events = serverMetadata
            if (events != null && events.size > 0) {
                (listRoot.adapter as ServerItemAdapter).setServerAlert(events[0])
            }
            idlingResource.decrement()
        }
    }

    override fun onSourceSelectionChanged(selectedNamespace: Namespace) {
        volley.cancelAll(this)
        downloadServers()
    }

    companion object {
        const val TAG = "FragmentServerList"
    }
}
