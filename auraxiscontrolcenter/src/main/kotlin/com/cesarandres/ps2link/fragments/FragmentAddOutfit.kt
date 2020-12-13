package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.dbg.view.LoadingItemAdapter
import com.cesarandres.ps2link.dbg.view.OutfitItemAdapter
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.Outfit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * This fragment will show the user a field to search for outfits based on their
 * tag or name. The tag has the limitation that it has to be at least three
 * characters long. When an outfit is found, it's content is cached into the
 * database.
 */
class FragmentAddOutfit : BasePS2Fragment(), SourceSelectionChangedListener {

    private lateinit var selectionButton: ButtonSelectSource
    private var lastUsedNamespace: Namespace? = null

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
        val view = inflater.inflate(R.layout.fragment_add_outfit, container, false)
        selectionButton = ButtonSelectSource(
            activity!!,
            activity!!.findViewById<View>(R.id.linearLayoutTitle) as ViewGroup,
            metrics,
        )
        selectionButton!!.listener = this
        return view
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
        val buttonOutfits =
            activity!!.findViewById<View>(R.id.imageButtonSearchOutfit) as ImageButton
        buttonOutfits.setOnClickListener {
            metrics.log(TAG, "Search")
            downloadOutfits()
        }

        this.fragmentUpdate.setOnClickListener {
            metrics.log(TAG, "Update")
            downloadOutfits()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val titleLayout = activity!!.findViewById<View>(R.id.linearLayoutTitle) as LinearLayout
        selectionButton!!.removeButtons(activity!!, titleLayout)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        activityContainer.activityMode = ActivityMode.ACTIVITY_ADD_OUTFIT
        this.fragmentUpdate.visibility = View.VISIBLE
    }

    /**
     * This method will retrieve the outfits based on the criteria given by the
     * user in the text fields. The user needs to provide a name or a tag or
     * both to start a search. If a value is provided but it is less than three
     * characters long, then the user will see a toast asking to provide more
     * information.
     */
    fun downloadOutfits() {
        this.lastUsedNamespace = selectionButton!!.namespace

        val searchField = activity!!.findViewById<View>(R.id.fieldSearchOutfit) as EditText
        val searchTagField = activity!!.findViewById<View>(R.id.fieldSearchTag) as EditText
        val outfitName = searchField.text.toString().toLowerCase(Locale.getDefault())
        val outfitTag = searchTagField.text.toString().toLowerCase(Locale.getDefault())

        // Check if the input values are valid
        if (!outfitTag.isEmpty() && outfitTag.length < 3) {
            Toast.makeText(activity, R.string.text_tag_too_short, Toast.LENGTH_SHORT).show()
        }
        if (!outfitName.isEmpty() && outfitName.length < 3) {
            Toast.makeText(activity, R.string.text_outfit_name_too_short, Toast.LENGTH_SHORT).show()
        }
        if (outfitName.length < 3 && outfitTag.length < 3) {
            // Clear the loading adapter
            return
        }

        val listRoot = activity!!.findViewById<View>(R.id.listFoundOutfits) as ListView
        listRoot.onItemClickListener = null
        // Set the loading adapter while searching
        listRoot.adapter = LoadingItemAdapter(activity!!)

        viewLifecycleOwner.lifecycleScope.launch {
            val outfitList = withContext(Dispatchers.IO) { dbgCensus.getOutfitList(outfitTag, outfitName, selectionButton.namespace!!, CensusLang.EN) }

            val listRoot = activity!!.findViewById<View>(R.id.listFoundOutfits) as ListView
            listRoot.adapter = OutfitItemAdapter(activity!!, outfitList!!)

            listRoot.onItemClickListener =
                OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
                    mCallbacks!!.onItemSelected(
                        ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
                        arrayOf(
                            (myAdapter.getItemAtPosition(myItemInt) as Outfit).outfit_id,
                            lastUsedNamespace!!.name
                        )
                    )
                }

            // Add the new outfits to the local cache
            val currentTask = UpdateTmpOutfitTable()
            setCurrentTask(currentTask)
            currentTask.execute(outfitList)
            listRoot.isTextFilterEnabled = true
        }
    }

    override fun onSourceSelectionChanged(selectedNamespace: Namespace) {
        downloadOutfits()
    }

    /**
     * This task will add the searched outfits to database. All outfits are
     * added to the database for the first time with the Temp flag set.
     */
    private inner class UpdateTmpOutfitTable : AsyncTask<List<Outfit>, Int, Boolean>() {

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
        override fun doInBackground(vararg outfits: List<Outfit>): Boolean? {
            val count = outfits[0].size
            val list = outfits[0]
            val data = activityContainer.data
            var outfit: Outfit? = null
            for (i in 0 until count) {
                outfit = data!!.getOutfit(list[i].outfit_id!!)
                // If outfit is not in cache
                if (outfit == null) {
                    val newOutfit = list[i]
                    newOutfit.namespace = lastUsedNamespace
                    data.insertOutfit(newOutfit, true)
                } else {
                    // If not, update the record
                    if (outfit.isCached) {
                        data.updateOutfit(list[i], false)
                    } else {
                        data.updateOutfit(list[i], true)
                    }
                }
            }
            return true
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Boolean?) {
            setProgressButton(false)
        }
    }

    companion object {
        const val TAG = "FragmentAddOutfit"
    }
}
