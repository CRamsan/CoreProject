package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.content.Outfit
import com.cesarandres.ps2link.dbg.view.OutfitItemAdapter

import java.util.ArrayList

/**
 * This fragment will read and display all the outfits that have been set as
 * non-temporary in the database.
 */
class FragmentOutfitList : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_outfit_list, container, false)
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
        this.fragmentTitle.text = getString(R.string.title_outfits)
        this.fragmentAdd.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_ADD_OUTFIT.toString(),
                null!!
            )
        }
        this.fragmentUpdate.setOnClickListener { ReadOutfitsTable().execute() }
        val listRoot = activity!!.findViewById<View>(R.id.listViewOutfitList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
                arrayOf(
                    (myAdapter.getItemAtPosition(myItemInt) as Outfit).outfit_Id,
                    (myAdapter.getItemAtPosition(myItemInt) as Outfit).namespace!!.name
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
        activityContainer.activityMode = ActivityMode.ACTIVITY_OUTFIT_LIST
        this.fragmentAdd.visibility = View.VISIBLE
        this.fragmentUpdate.visibility = View.VISIBLE
        val task = ReadOutfitsTable()
        setCurrentTask(task)
        task.execute()
    }

    /**
     * This task will read all outfits that have not been set as temporary in
     * the database
     */
    private inner class ReadOutfitsTable : AsyncTask<Int, Int, ArrayList<Outfit>>() {

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
        protected override fun doInBackground(vararg params: Int?): ArrayList<Outfit> {
            var outfitList = ArrayList<Outfit>()
            val data = activityContainer.data
            outfitList = data!!.getAllOutfits(false)
            data.deleteAllOutfit(false)
            return outfitList
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: ArrayList<Outfit>) {
            val listRoot = activity!!.findViewById<View>(R.id.listViewOutfitList) as ListView
            listRoot.adapter = OutfitItemAdapter(activity!!, result)
            setProgressButton(false)
        }
    }
}
