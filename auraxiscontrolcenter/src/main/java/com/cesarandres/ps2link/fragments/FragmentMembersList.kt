package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast

import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.Member
import com.cesarandres.ps2link.dbg.content.response.Outfit_member_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier
import com.cesarandres.ps2link.dbg.view.MemberItemAdapter

import java.util.ArrayList

/**
 * Fargment that will retrieve and display all the members of an outfit in
 * alphabetical order. This fragment allows to display online member or all
 * members.
 */
class FragmentMembersList : BaseFragment() {

    private val isCached: Boolean = false
    private var shownOffline = false
    private var outfitSize: Int = 0
    private var outfitId: String? = null
    private var outfitName: String? = null

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

        // Check if outfit data has already been loaded
        if (savedInstanceState == null) {
            this.outfitId = arguments!!.getString("PARAM_0")
        } else {
            this.outfitSize = savedInstanceState.getInt("outfitSize", 0)
            this.outfitId = savedInstanceState.getString("outfitId")
            this.outfitName = savedInstanceState.getString("outfitName")
            this.shownOffline = savedInstanceState.getBoolean("showOffline")
        }

        this.fragmentShowOffline.setOnCheckedChangeListener { buttonView, isChecked ->
            shownOffline = isChecked
            updateContent()
        }
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
        setProgressButton(true)
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET,
            PS2Collection.OUTFIT,
            "",
            QueryString.generateQeuryString().AddComparison(
                "outfit_id",
                SearchModifier.EQUALS,
                this.outfitId!!
            )
                .AddCommand(
                    QueryCommand.RESOLVE,
                    "member_online_status,member,member_character(name,type.faction)"
                )
        )!!.toString()

        val success = Listener<Outfit_member_response> { response ->
            setProgressButton(false)
            try {
                val task = UpdateMembers()
                setCurrentTask(task)
                val list = response.outfit_list!![0].members
                // Check this warning
                task.execute(list)
            } catch (e: Exception) {
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }
        dbgCensus.sendGsonRequest(url, Outfit_member_response::class.java, success, error, this)
    }

    /**
     * This method will set the adapter that will read outfit members from the
     * database
     */
    private fun updateContent() {
        val listRoot = view!!.findViewById<View>(R.id.listViewMemberList) as ListView
        val data = activityContainer.data
        listRoot.adapter = MemberItemAdapter(activity!!, outfitId!!, data!!, shownOffline)

        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            mCallbacks!!.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as Member).character_id,
                    dbgCensus.currentNamespace.name
                )
            )
        }

    }

    /**
     * This Async task will replace the old member information with new one. The
     * process will remove all previous members in the outfit and write the new
     * ones. This task can take a long time, specially on big outfits and on old
     * devices
     */
    private inner class UpdateMembers : AsyncTask<ArrayList<Member>, Int, Int>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            setProgressButton(true)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg members: ArrayList<Member>): Int? {
            val newMembers = members[0]
            val data = activityContainer.data
            val outfit = data!!.getOutfit(outfitId!!) ?: return null
            outfit.member_count = newMembers.size
            data.updateOutfit(outfit, !outfit.isCached)

            data.deleteAllMembers(outfitId)
            for (member in newMembers) {
                data.insertMember(member, outfitId!!, !isCached)
                if (isCancelled) {
                    return null
                }
            }
            return null
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Int?) {
            setProgressButton(false)
            updateContent()
        }
    }
}
