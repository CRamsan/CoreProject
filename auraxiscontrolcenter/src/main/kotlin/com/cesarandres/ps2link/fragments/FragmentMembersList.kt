package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentMemberListBinding
import com.cesarandres.ps2link.dbg.view.MemberItemAdapter
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fargment that will retrieve and display all the members of an outfit in
 * alphabetical order. This fragment allows to display online member or all
 * members.
 */
class FragmentMembersList : BasePS2Fragment<NoopViewModel, FragmentMemberListBinding>() {

    private val isCached: Boolean = false
    private var shownOffline = false
    private var outfitSize: Int = 0
    private lateinit var outfitId: String
    private var outfitName: String? = null
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

        // Check if outfit data has already been loaded
        if (savedInstanceState == null) {
            this.outfitId = requireArguments().getString("PARAM_0")!!
        } else {
            this.outfitSize = savedInstanceState.getInt("outfitSize", 0)
            this.outfitId = savedInstanceState.getString("outfitId")!!
            this.outfitName = savedInstanceState.getString("outfitName")
            this.shownOffline = savedInstanceState.getBoolean("showOffline")
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
        savedInstanceState.putInt("outfitSize", outfitSize)
        savedInstanceState.putString("outfitId", outfitId)
        savedInstanceState.putString("outfitName", outfitName)
        savedInstanceState.putBoolean("showOffline", shownOffline)
    }

    /**
     * This method will retrieve the members for the given outfit_id and it will
     * start a task to cache that data
     */
    fun downloadOutfitMembers() {

        lifecycleScope.launch {
            val membersList = withContext(Dispatchers.IO) { dbgCensus.getMemberList(outfitId, namespace!!, CensusLang.EN) }
            val list = membersList
            // Check this warning
        }
    }

    /**
     * This method will set the adapter that will read outfit members from the
     * database
     */
    private fun updateContent() {
        val listRoot = requireView().findViewById<View>(R.id.listViewMemberList) as ListView
        listRoot.adapter = MemberItemAdapter(requireActivity())

        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            TODO()
        }
    }

    companion object {
        const val TAG = "FragmentMembersList"
    }

    override val logTag = "FragmentMembersList"
    override val contentViewLayout = R.layout.fragment_member_list
}
