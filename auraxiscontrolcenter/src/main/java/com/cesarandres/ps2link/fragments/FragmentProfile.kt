package com.cesarandres.ps2link.fragments

import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.CharacterProfile
import com.cesarandres.ps2link.dbg.content.Faction
import com.cesarandres.ps2link.dbg.content.response.Character_list_response
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.logging.Severity
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

/**
 * This fragment will read a profile from the database and display it to the
 * user. It will then try to update the data by doing a query to the API
 */
class FragmentProfile : BaseFragment() {

    private var isCached: Boolean = false
    private var profile: CharacterProfile? = null
    private var profileId: String? = null
    private var namespace: DBGCensus.Namespace? = null

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val task = UpdateProfileFromTable()
        setCurrentTask(task)
        this.profileId = arguments!!.getString("PARAM_0")
        this.namespace = DBGCensus.Namespace.valueOf(arguments!!.getString("PARAM_1"))
        task.execute(this.profileId)
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    /**
     * @param character Character that contains all the data to populate the UI
     */
    private fun updateUI(character: CharacterProfile) {
        idlingResource.increment()
        eventLogger.log(Severity.INFO, TAG, "Updating UI")
        this.fragmentTitle.text = character.name!!.first
        try {
            if (this.view != null) {
                val faction =
                    activity!!.findViewById<View>(R.id.imageViewProfileFaction) as ImageView
                if (character.faction_id == Faction.VS) {
                    faction.setImageResource(R.drawable.icon_faction_vs)
                } else if (character.faction_id == Faction.NC) {
                    faction.setImageResource(R.drawable.icon_faction_nc)
                } else if (character.faction_id == Faction.TR) {
                    faction.setImageResource(R.drawable.icon_faction_tr)
                }

                val initialBR = activity!!.findViewById<View>(R.id.textViewCurrentRank) as TextView
                initialBR.text = Integer.toString(character.battle_rank!!.value)
                initialBR.setTextColor(Color.BLACK)

                val nextBR = activity!!.findViewById<View>(R.id.textViewNextRank) as TextView
                nextBR.text = Integer.toString(character.battle_rank!!.value + 1)
                nextBR.setTextColor(Color.BLACK)

                val progressBR = character.battle_rank!!.percent_to_next
                (activity!!.findViewById<View>(R.id.progressBarProfileBRProgress) as ProgressBar).progress =
                    progressBR

                val progressCerts = java.lang.Float.parseFloat(character.certs!!.percent_to_next!!)
                (activity!!.findViewById<View>(R.id.progressBarProfileCertsProgress) as ProgressBar).progress =
                    (progressCerts * 100).toInt()
                val certs =
                    activity!!.findViewById<View>(R.id.textViewProfileCertsValue) as TextView
                certs.text = character.certs!!.available_points

                val loginStatus =
                    activity!!.findViewById<View>(R.id.TextViewProfileLoginStatusText) as TextView
                var onlineStatusText = activity!!.resources.getString(R.string.text_unknown)
                if (character.online_status == 0) {
                    onlineStatusText = activity!!.resources.getString(R.string.text_offline_caps)
                    loginStatus.text = onlineStatusText
                    loginStatus.setTextColor(Color.RED)
                } else {
                    onlineStatusText = activity!!.resources.getString(R.string.text_online_caps)
                    loginStatus.text = onlineStatusText
                    loginStatus.setTextColor(Color.GREEN)
                }

                (activity!!.findViewById<View>(R.id.textViewProfileMinutesPlayed) as TextView).text =
                    Integer.toString(
                        Integer.parseInt(
                            character.times!!
                                .minutes_played!!
                        ) / 60
                    )

                val p = PrettyTime()
                val lastLogin =
                    p.format(Date(java.lang.Long.parseLong(character.times!!.last_login!!) * 1000))

                (activity!!.findViewById<View>(R.id.textViewProfileLastLogin) as TextView).text =
                    lastLogin

                val outfitButton =
                    activity!!.findViewById<View>(R.id.buttonProfileToOutfit) as Button

                if (character.outfitName != null) {
                    outfitButton.text = character.outfitName
                }

                if (character.outfit == null) {
                    outfitButton.isEnabled = false
                    outfitButton.alpha = 0.5f
                    outfitButton.setOnClickListener(null)
                } else {
                    outfitButton.text = character.outfit!!.name
                    outfitButton.isEnabled = true
                    outfitButton.alpha = 1f
                    outfitButton.setOnClickListener {
                        metrics.log(TAG, "Open Outfit")
                        mCallbacks!!.onItemSelected(
                            ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
                            arrayOf(character.outfit!!.outfit_id, character.namespace!!.name)
                        )
                    }
                }

                if (character.server != null) {
                    (activity!!.findViewById<View>(R.id.textViewServerText) as TextView).text =
                        character.server!!.name!!.localizedName(dbgCensus.currentLang)
                } else {
                    (activity!!.findViewById<View>(R.id.textViewServerText) as TextView).text =
                        activity!!.resources.getString(R.string.text_unknown)
                }
            }

            this.fragmentStar.setOnCheckedChangeListener(null)
            val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
            val preferedProfileId = settings.getString("preferedProfile", "")
            if (preferedProfileId == character.character_id) {
                this.fragmentStar.isChecked = true
            } else {
                this.fragmentStar.isChecked = false
            }

            this.fragmentStar.setOnCheckedChangeListener { buttonView, isChecked ->
                val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
                val editor = settings.edit()
                if (isChecked) {
                    editor.putString("preferedProfile", profile!!.character_id)
                    editor.putString("preferedProfileName", profile!!.name!!.first)
                    editor.putString("preferedProfileNamespace", character.namespace!!.name)
                } else {
                    editor.putString("preferedProfileName", "")
                    editor.putString("preferedProfile", "")
                    editor.putString("preferedProfileNamespace", "")
                }
                editor.commit()
                activityContainer.checkPreferedButtons()
            }

            this.fragmentAppend.setOnCheckedChangeListener(null)
            this.fragmentAppend.isChecked = isCached
            this.fragmentAppend.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val task = CacheProfile()
                    setCurrentTask(task)
                    task.execute(profile)
                } else {
                    val task = UnCacheProfile()
                    setCurrentTask(task)
                    task.execute(profile)
                }
            }
        } catch (e: NullPointerException) {
            metrics.log(TAG, "NPE when updating the UI")
            eventLogger.log(Severity.ERROR, TAG, "Null Pointer while trying to set character data on UI")
        }
        eventLogger.log(Severity.INFO, TAG, "UI was updated")
        idlingResource.decrement()
    }

    /**
     * @param character_id Character id of the character that wants to be download
     */
    fun downloadProfiles(character_id: String?) {
        idlingResource.increment()
        eventLogger.log(Severity.INFO, TAG, "Downloading Profile")
        this.setProgressButton(true)
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET,
            PS2Collection.CHARACTER,
            character_id,
            QueryString.generateQeuryString().AddCommand(
                QueryCommand.RESOLVE,
                "outfit,world,online_status"
            )
                .AddCommand(QueryCommand.JOIN, "type:world^inject_at:server"),
            this.namespace!!
        )!!.toString()
        val success = Listener<Character_list_response> { response ->
            setProgressButton(false)
            try {
                eventLogger.log(Severity.INFO, TAG, "Profile Downloaded")
                profile = response.character_list!![0]
                profile!!.namespace = this.namespace!!
                profile!!.isCached = isCached
                updateUI(profile!!)
                val task = UpdateProfileToTable()
                setCurrentTask(task)
                task.execute(profile)
            } catch (e: Exception) {
                metrics.log(TAG, Constants.ERROR_PARSING_RESPONE)
                eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_PARSING_RESPONE)
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
            idlingResource.decrement()
        }

        val error = ErrorListener {
            setProgressButton(false)
            metrics.log(TAG, Constants.ERROR_MAKING_REQUEST)
            eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_MAKING_REQUEST)
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
            idlingResource.decrement()
        }

        dbgCensus.sendGsonRequest(url, Character_list_response::class.java, success, error, this)
    }

    /**
     * Read the profile from the database and update the UI
     */
    private inner class UpdateProfileFromTable : AsyncTask<String, Int, CharacterProfile>() {

        private var profile_id: String? = null

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            idlingResource.increment()
            eventLogger.log(Severity.INFO, TAG, "UpdateProfileFromTable PreExecute")
            setProgressButton(true)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg args: String): CharacterProfile? {
            this.profile_id = args[0]
            val data = activityContainer.data
            val profile = data!!.getCharacter(this.profile_id!!)
            if (profile == null) {
                isCached = false
            } else {
                isCached = profile.isCached
            }
            return profile
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: CharacterProfile?) {
            setProgressButton(false)
            eventLogger.log(Severity.INFO, TAG, "UpdateProfileFromTable PostExecute")
            if (result == null) {
                downloadProfiles(profile_id)
            } else {
                profile = result
                updateUI(result)
                downloadProfiles(result.character_id)
            }
            idlingResource.decrement()
        }
    }

    /**
     * Save the profile to the database
     */
    private inner class UpdateProfileToTable :
        AsyncTask<CharacterProfile, Int, CharacterProfile>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            idlingResource.increment()
            eventLogger.log(Severity.INFO, TAG, "UpdateProfileToTable PreExecute")
            setProgressButton(true)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg args: CharacterProfile): CharacterProfile? {
            var profile: CharacterProfile? = null
            val data = activityContainer.data
            try {
                profile = args[0]
                if (data!!.getCharacter(profileId!!) != null) {
                    data.updateCharacter(profile, !profile.isCached)
                } else {
                    data.insertCharacter(profile, !profile.isCached)
                }

                if (profile.outfit != null) {
                    var outfit = data.getOutfit(profile.outfit!!.outfit_id!!)
                    if (outfit == null) {
                        outfit = profile.outfit
                        data.insertOutfit(outfit!!, true)
                    }
                }
            } catch (e: Exception) {
            }

            return profile
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: CharacterProfile) {
            setProgressButton(false)
            idlingResource.decrement()
            eventLogger.log(Severity.INFO, TAG, "UpdateProfileToTable PostExecute")
        }
    }

    /**
     * Save the profile in the database and set it as not temporary
     */
    private inner class CacheProfile : AsyncTask<CharacterProfile, Int, CharacterProfile>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            idlingResource.increment()
            eventLogger.log(Severity.INFO, TAG, "CacheProfile PreExecute")
            setProgressButton(true)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg args: CharacterProfile): CharacterProfile {
            val profile = args[0]
            val data = activityContainer.data
            try {
                if (data!!.getCharacter(profile.character_id!!) == null) {
                    data.insertCharacter(profile, false)
                } else {
                    data.updateCharacter(profile, false)
                }
                isCached = true
            } finally {
            }
            return profile
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: CharacterProfile) {
            eventLogger.log(Severity.INFO, TAG, "CacheProfile PostExecute")
            profile = result
            updateUI(result)
            setProgressButton(false)
            idlingResource.decrement()
        }
    }

    /**
     * Update the database profile and set the profile as temporary
     */
    private inner class UnCacheProfile : AsyncTask<CharacterProfile, Int, CharacterProfile>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
            idlingResource.increment()
            eventLogger.log(Severity.INFO, TAG, "UnCacheProfile PreExecute")
            setProgressButton(true)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg args: CharacterProfile): CharacterProfile? {
            val data = activityContainer.data
            try {
                val profile = args[0]
                data!!.updateCharacter(profile, true)
                isCached = false
            } catch (e: Exception) {
            }

            return profile
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: CharacterProfile) {
            profile = result
            updateUI(result)
            setProgressButton(false)
            eventLogger.log(Severity.INFO, TAG, "UnCacheProfile PostExecute")
            idlingResource.decrement()
        }
    }

    companion object {
        const val TAG = "FragmentProfile"
    }
}
