package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentMemberListBinding
import com.cesarandres.ps2link.dbg.view.OnlineMemberItemAdapter
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will do a request to retrieve all members for the given outfit
 * and resolve the class they are using. This is very useful to show who is
 * online and display their current class
 */
class FragmentMembersOnline : BasePS2Fragment<NoopViewModel, FragmentMemberListBinding>() {

    override val viewModel: NoopViewModel by viewModels()
    private lateinit var outfitId: String
    private val outfitName: String? = null
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

        if (savedInstanceState == null) {
            this.outfitId = requireArguments().getString("PARAM_0")!!
        } else {
            this.outfitId = savedInstanceState.getString("outfitId")!!
        }
        this.namespace = Namespace.valueOf(requireArguments().getString("PARAM_1", ""))
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

        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) { dbgCensus.getMembersOnline(outfitId, namespace!!, CensusLang.EN) }
            updateContent(response)
        }
    }

    /**
     * @param members An array list with all the members found. The adapter will
     * retrieve all online members and it will only display those
     */
    private fun updateContent(members: List<Member>?) {
        val listRoot = requireView().findViewById<View>(R.id.listViewMemberList) as ListView

        listRoot.adapter = OnlineMemberItemAdapter(members!!, requireActivity())
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            TODO()
        }
    }

    companion object {
        const val TAG = "FragmentMembersOnline"
    }

    override val logTag = "FragmentMembersOnline"
    override val contentViewLayout = R.layout.fragment_member_list
}
