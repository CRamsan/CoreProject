package com.cesarandres.ps2link.fragments.profilepager.friendlist

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentFriendListBinding
import com.cesarandres.ps2link.dbg.view.FriendItemAdapter
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.toNetworkModel
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will display the friends of a given user. This fragment is
 * designed to be part of a profile pager.
 */
class FragmentFriendList : BasePS2Fragment<NoopViewModel, FragmentFriendListBinding>() {

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
        val listRoot = requireActivity().findViewById<View>(R.id.listViewFriendList) as ListView
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
        downloadFriendsList(this.profileId)
    }

    /**
     * @param character_id Character id that will be used to request the list of friends
     */
    fun downloadFriendsList(character_id: String?) {

        viewLifecycleOwner.lifecycleScope.launch {
            val friendList = withContext(Dispatchers.IO) { dbgCensus.getFriendList(character_id!!, namespace!!.toNetworkModel(), CensusLang.EN) }

            val listRoot = requireActivity().findViewById<View>(R.id.listViewFriendList) as ListView
            listRoot.adapter =
                FriendItemAdapter(requireActivity(), friendList!!)
        }
    }

    companion object {
        const val TAG = "FragmentFriendList"
    }

    override val logTag = "FragmentFriendList"
    override val contentViewLayout = R.layout.fragment_friend_list
}
