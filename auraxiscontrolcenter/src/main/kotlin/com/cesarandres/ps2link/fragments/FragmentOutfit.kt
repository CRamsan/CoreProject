package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cramsan.framework.logging.Severity
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.Faction
import com.cramsan.ps2link.appcore.dbg.content.Outfit
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import java.util.Locale

/**
 * This fragment will read the outfit from the database to display it to the user. Then a network
 * request will be made to get updated information.
 */
class FragmentOutfit : BasePS2Fragment() {

    private var isCached: Boolean = false
    private var outfitId: String? = null
    private var outfit: Outfit? = null
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
        val task = UpdateOutfitFromTable()
        setCurrentTask(task)
        this.outfitId = arguments!!.getString("PARAM_0")
        this.namespace = Namespace.valueOf(arguments!!.getString("PARAM_1", ""))
        task.execute(this.outfitId)
    }

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
        return inflater.inflate(R.layout.fragment_outfit, container, false)
    }

    /**
     * @param character Character that contains all the data to populate the UI
     */
    private fun updateUI(outfit: Outfit) {
        this.fragmentTitle.text = outfit.name
        try {
            if (this.view != null) {
                val outfitName =
                    activity!!.findViewById<View>(R.id.textViewFragmentOutfitName) as TextView
                outfitName.text = "[" + outfit.alias + "] " + outfit.name

                val outfitSize = activity!!.findViewById<View>(R.id.textViewMembersText) as TextView
                outfitSize.text = Integer.toString(outfit.member_count)

                val outfitCreation =
                    activity!!.findViewById<View>(R.id.TextViewOutfitCreationText) as TextView
                val date = Date(java.lang.Long.parseLong(outfit.time_created!!) * 1000)

                val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
                outfitCreation.text = df.format(date)

                val leaderButton =
                    activity!!.findViewById<View>(R.id.buttonOutfitToLeader) as Button

                if (outfit.leader != null) {
                    val faction =
                        activity!!.findViewById<View>(R.id.imageViewOutfitFaction) as ImageView
                    if (outfit.leader!!.faction_id == Faction.VS) {
                        faction.setImageResource(R.drawable.icon_faction_vs)
                    } else if (outfit.leader!!.faction_id == Faction.NC) {
                        faction.setImageResource(R.drawable.icon_faction_nc)
                    } else if (outfit.leader!!.faction_id == Faction.TR) {
                        faction.setImageResource(R.drawable.icon_faction_tr)
                    }

                    leaderButton.text = outfit.leader!!.name!!.first
                }

                if (outfit.leader_character_id != null) {
                    leaderButton.isEnabled = true
                    leaderButton.alpha = 1f
                    leaderButton.setOnClickListener {
                        metrics.log(TAG, "Open Outfit Leader")
                        mCallbacks!!.onItemSelected(
                            ActivityMode.ACTIVITY_PROFILE.toString(),
                            arrayOf(outfit.leader_character_id, this.namespace!!.name)
                        )
                    }
                }
            }

            this.fragmentStar.setOnCheckedChangeListener(null)
            val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
            val preferedOutfitId = settings.getString("preferedOutfit", "")
            if (preferedOutfitId == outfit.outfit_id) {
                this.fragmentStar.isChecked = true
            } else {
                this.fragmentStar.isChecked = false
            }

            this.fragmentStar.setOnCheckedChangeListener { buttonView, isChecked ->
                val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
                val editor = settings.edit()
                if (isChecked) {
                    editor.putString("preferedOutfit", outfit.outfit_id)
                    editor.putString("preferedOutfitName", outfit.name)
                    editor.putString("preferedOutfitNamespace", this.namespace!!.name)
                } else {
                    editor.putString("preferedOutfitName", "")
                    editor.putString("preferedOutfit", "")
                    editor.putString("preferedOutfitNamespace", "")
                }
                editor.commit()
                activityContainer.checkPreferedButtons()
            }

            this.fragmentAppend.setOnCheckedChangeListener(null)
            this.fragmentAppend.isChecked = isCached
            this.fragmentAppend.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val task = CacheOutfit()
                    setCurrentTask(task)
                    task.execute(outfitId, "true")
                } else {
                    val task = CacheOutfit()
                    setCurrentTask(task)
                    task.execute(outfitId, "false")
                }
            }
        } catch (e: NullPointerException) {
            metrics.log(TAG, "NPE when updating the UI")
            eventLogger.log(Severity.ERROR, TAG, "Null Pointer while trying to set character data on UI")
        }
    }

    /**
     * @param character_id Character id of the character that wants to be download
     */
    fun downloadOutfit(outfitId: String?) {
        this.setProgressButton(true)

        lifecycleScope.launch {
            val outfit = dbgCensus.getOutfit(outfitId!!, namespace!!, CensusLang.EN)
            setProgressButton(false)
            updateUI(outfit!!)
            val task = UpdateOutfitToTable()
            setCurrentTask(task)
            task.execute(outfit)
        }
    }

    /**
     * Read the profile from the database and update the UI
     */
    private inner class UpdateOutfitFromTable : AsyncTask<String, Int, Outfit>() {

        private var outfit_id: String? = null

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
        override fun doInBackground(vararg args: String): Outfit? {
            this.outfit_id = args[0]
            val data = activityContainer.data
            val outfit = data!!.getOutfit(this.outfit_id!!)
            if (outfit == null) {
                isCached = false
            } else {
                isCached = outfit.isCached
            }
            return outfit
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Outfit?) {
            setProgressButton(false)
            if (result == null) {
                downloadOutfit(this.outfit_id)
            } else {
                outfit = result
                updateUI(result)
                downloadOutfit(result.outfit_id)
            }
        }
    }

    /**
     * Save the profile to the database
     */
    private inner class UpdateOutfitToTable : AsyncTask<Outfit, Int, Outfit>() {

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
        override fun doInBackground(vararg args: Outfit): Outfit? {
            var outfit: Outfit? = null
            val data = activityContainer.data
            try {
                outfit = args[0]
                if (data!!.getOutfit(outfitId!!) != null) {
                    data.updateOutfit(outfit, !outfit.isCached)
                } else {
                    data.insertOutfit(outfit, !outfit.isCached)
                }
            } catch (e: Exception) {
            }

            return outfit
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Outfit) {
            setProgressButton(false)
        }
    }

    /**
     * This task will set an outfit as temp or not. The first argument needs to
     * be the outfit_id and the second a string with true or false, true will
     * save the outfit and display it on the outfit list, false will save the
     * outfit in the databse but it will not be displayed on the outfit list.
     */
    private inner class CacheOutfit : AsyncTask<String, Int, Int>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            setProgressButton(true)
        }

        override fun doInBackground(vararg args: String): Int? {
            val data = activityContainer.data
            val outfit = data!!.getOutfit(args[0])
            val temp = !java.lang.Boolean.parseBoolean(args[1])
            data.updateOutfit(outfit!!, temp)
            isCached = !temp
            return 0
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Int?) {
            if (isCached) {
                updateUI(outfit!!)
            }
            setProgressButton(false)
        }
    }

    companion object {
        const val TAG = "FragmentOutfit"
    }
}
