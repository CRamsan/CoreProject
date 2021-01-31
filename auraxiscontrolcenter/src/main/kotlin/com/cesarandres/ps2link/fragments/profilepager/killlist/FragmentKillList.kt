package com.cesarandres.ps2link.fragments.profilepager.killlist

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentKillListBinding
import com.cesarandres.ps2link.dbg.view.KillItemAdapter
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.toNetworkModel
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will retrieve the killboard of a player and display it.
 */
class FragmentKillList : BasePS2Fragment<NoopViewModel, FragmentKillListBinding>() {

    override val viewModel: NoopViewModel by viewModels()
    private var profileId: String? = null
    private var namespace: Namespace? = null

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val listRoot = requireActivity().findViewById<View>(R.id.listViewKillList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            TODO()
        }

        this.profileId = requireArguments().getString("PARAM_0")
        this.namespace = Namespace.valueOf(requireArguments().getString("PARAM_1", ""))
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

        viewLifecycleOwner.lifecycleScope.launch {
            val eventList = withContext(Dispatchers.IO) { dbgCensus.getKillList(character_id!!, namespace!!.toNetworkModel(), CensusLang.EN) }

            if (eventList == null) {
                return@launch
            }
            val listRoot = requireActivity().findViewById<View>(R.id.listViewKillList) as ListView
            listRoot.adapter =
                KillItemAdapter(
                    requireActivity(),
                    eventList!!,
                    profileId!!,
                    namespace!!,
                    dbgCensus
                )
        }
    }

    companion object {
        const val TAG = "FragmentKillList"
    }

    override val logTag = "FragmentKillList"

    override val contentViewLayout = R.layout.fragment_kill_list
}
