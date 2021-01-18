package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentServerListBinding
import com.cesarandres.ps2link.dbg.view.ServerItemAdapter
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will display the servers and theirs status
 */
class FragmentServerList : BasePS2Fragment<NoopViewModel, FragmentServerListBinding>(), SourceSelectionChangedListener {

    override val viewModel: NoopViewModel by viewModels()
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
        super.onCreateView(inflater, container, savedInstanceState)

        selectionButton!!.listener = this
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listRoot = requireActivity().findViewById<View>(R.id.listViewServers) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val listParent = requireActivity().findViewById<View>(R.id.listViewServers) as ListView
            (listParent.adapter as ServerItemAdapter).onItemSelected(myItemInt, requireContext())
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadServers()
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    fun downloadServers() {

        lifecycleScope.launch {
            val serverList = withContext(Dispatchers.IO) { dbgCensus.getServerList(selectionButton!!.namespace, CensusLang.EN) }
            val listRoot = requireActivity().findViewById<View>(R.id.listViewServers) as ListView
            listRoot.adapter = ServerItemAdapter(requireActivity(), serverList!!, dbgCensus, selectionButton!!.namespace)
            for (world in serverList) {
                downloadServerAlert(world.world_id)
            }

            downloadServerPopulation()
        }
    }

    /**
     * Query the API and retrieve the latest population info for all the servers
     * and update the UI. This method uses a non-standard API call. This API
     * call has been unreliable in the past.
     */
    fun downloadServerPopulation() {

        lifecycleScope.launch {
            val serverPopulation = withContext(Dispatchers.IO) { dbgCensus.getServerPopulation() }

            val listRoot = requireActivity().findViewById<View>(R.id.listViewServers) as ListView
            val servers = serverPopulation
            (listRoot.adapter as ServerItemAdapter).setServerPopulation(servers!!)
        }
    }

    /**
     * This call will request the last METAGEME event less than 15 minutes old for this given server
     */
    private fun downloadServerAlert(serverId: String?) {

        lifecycleScope.launch {
            val serverMetadata = withContext(Dispatchers.IO) { dbgCensus.getServerMetadata(serverId!!, selectionButton!!.namespace, CensusLang.EN) }

            val listRoot = requireActivity().findViewById<View>(R.id.listViewServers) as ListView
            val events = serverMetadata
            if (events != null && events.size > 0) {
                (listRoot.adapter as ServerItemAdapter).setServerAlert(events[0])
            }
        }
    }

    override fun onSourceSelectionChanged(selectedNamespace: Namespace) {
        downloadServers()
    }

    companion object {
        const val TAG = "FragmentServerList"
    }

    override val logTag = "FragmentServerList"
    override val contentViewLayout = R.layout.fragment_server_list
}
