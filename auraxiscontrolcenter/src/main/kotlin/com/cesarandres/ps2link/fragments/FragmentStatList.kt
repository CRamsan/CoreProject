package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.dbg.view.StatItemAdapter
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Retrieve the stats for the given character
 */
class FragmentStatList : BasePS2Fragment() {

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
        this.namespace = Namespace.valueOf(arguments!!.getString("PARAM_1", ""))
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
        lifecycleScope.launch {
            val statList = withContext(Dispatchers.IO) { dbgCensus.getStatList(character_id!!, namespace!!, CensusLang.EN) }
            val listRoot = activity!!.findViewById<View>(R.id.listViewStatList) as ListView
            listRoot.adapter = StatItemAdapter(
                activity!!,
                statList!!.stat_history!!,
                character_id!!
            )
        }
    }

    companion object {
        const val TAG = "FragmentStatList"
    }
}
