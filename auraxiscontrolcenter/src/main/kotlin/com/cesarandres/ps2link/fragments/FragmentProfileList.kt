package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.content.CharacterProfile
import com.cesarandres.ps2link.dbg.view.ProfileItemAdapter
import java.util.ArrayList

/**
 * Fragment that reads the profiles from the database that have been set as not
 * temporary
 */
class FragmentProfileList : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
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
        this.fragmentTitle.text = getString(R.string.title_profiles)
        this.fragmentUpdate.setOnClickListener {
            metrics.log(TAG, "Update")
            val task = ReadProfilesTable()
            setCurrentTask(task)
            task.execute()
        }
        this.fragmentAdd.setOnClickListener {
            metrics.log(TAG, "Add Profile")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_ADD_PROFILE.toString(),
                emptyArray()
            )
        }
        val listRoot = activity!!.findViewById<View>(R.id.listViewProfileList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val profile = (myAdapter.getItemAtPosition(myItemInt) as CharacterProfile)
            mCallbacks!!.onItemSelected(
                ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                arrayOf(
                    profile.character_id,
                    profile.namespace!!.name
                )
            )
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        activityContainer.activityMode = ActivityMode.ACTIVITY_PROFILE_LIST
        this.fragmentUpdate.visibility = View.VISIBLE
        this.fragmentAdd.visibility = View.VISIBLE
        val task = ReadProfilesTable()
        setCurrentTask(task)
        task.execute()
    }

    /**
     * Reads the profiles in the database that are set as non temporary
     */
    private inner class ReadProfilesTable : AsyncTask<Int, Int, ArrayList<CharacterProfile>>() {

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
        protected override fun doInBackground(vararg params: Int?): ArrayList<CharacterProfile>? {
            var tmpProfileList: ArrayList<CharacterProfile>? = null
            val data = activityContainer.data
            data!!.deleteAllCharacterProfiles(false)
            tmpProfileList = data.getAllCharacterProfiles(false)
            return tmpProfileList
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: ArrayList<CharacterProfile>?) {
            if (result != null) {
                val listRoot = activity!!.findViewById<View>(R.id.listViewProfileList) as ListView
                listRoot.adapter = ProfileItemAdapter(activity!!, result, true)
            }
            setProgressButton(false)
        }
    }

    companion object {
        const val TAG = "FragmentProfileList"
    }
}
