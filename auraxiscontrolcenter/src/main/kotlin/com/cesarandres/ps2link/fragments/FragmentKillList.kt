package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.dbg.view.KillItemAdapter
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.CharacterEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This fragment will retrieve the killboard of a player and display it.
 */
class FragmentKillList : BasePS2Fragment() {

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
        this.namespace = Namespace.valueOf(arguments!!.getString("PARAM_1", ""))
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
        viewLifecycleOwner.lifecycleScope.launch {
            val eventList = withContext(Dispatchers.IO) { dbgCensus.getKillList(character_id, namespace!!, CensusLang.EN) }
            setProgressButton(false)
            if (eventList == null) {
                return@launch
            }
            val listRoot = activity!!.findViewById<View>(R.id.listViewKillList) as ListView
            listRoot.adapter =
                KillItemAdapter(
                    activity!!,
                    eventList!!,
                    profileId!!,
                    namespace!!,
                    volley,
                    imageLoader,
                    dbgCensus
                )
        }
    }

    companion object {
        const val TAG = "FragmentKillList"
    }
}
