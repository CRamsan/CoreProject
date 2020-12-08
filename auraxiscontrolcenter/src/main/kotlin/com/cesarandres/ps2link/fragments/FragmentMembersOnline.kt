package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cramsan.ps2link.appcore.dbg.content.Member
import com.cramsan.ps2link.appcore.dbg.content.response.Outfit_member_response
import com.cesarandres.ps2link.dbg.view.OnlineMemberItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import java.util.ArrayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will do a request to retrieve all members for the given outfit
 * and resolve the class they are using. This is very useful to show who is
 * online and display their current class
 */
class FragmentMembersOnline : BaseFragment() {

    private lateinit var outfitId: String
    private val outfitName: String? = null
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
        return inflater.inflate(R.layout.fragment_member_list, container, false)
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

        if (savedInstanceState == null) {
            this.outfitId = arguments!!.getString("PARAM_0")!!
        } else {
            this.outfitId = savedInstanceState.getString("outfitId")!!
        }
        this.namespace = Namespace.valueOf(arguments!!.getString("PARAM_1", ""))

        this.fragmentTitle.text = outfitName
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
        setProgressButton(true)
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
        val listRoot = view!!.findViewById<View>(R.id.listViewMemberList) as ListView

        listRoot.adapter = OnlineMemberItemAdapter(members!!, activity!!)
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            mCallbacks!!.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as Member).character_id,
                    this.namespace!!.name
                )
            )
        }
    }

    companion object {
        const val TAG = "FragmentMembersOnline"
    }
}
