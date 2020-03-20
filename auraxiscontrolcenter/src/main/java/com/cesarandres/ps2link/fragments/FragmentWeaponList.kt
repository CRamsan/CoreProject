package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.CompoundButton
import android.widget.ListView
import android.widget.Toast

import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.content.CharacterProfile
import com.cesarandres.ps2link.dbg.content.Faction
import com.cesarandres.ps2link.dbg.content.item.Weapon
import com.cesarandres.ps2link.dbg.content.item.WeaponStat
import com.cesarandres.ps2link.dbg.content.response.Weapon_list_response
import com.cesarandres.ps2link.dbg.view.WeaponItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cesarandres.ps2link.module.ObjectDataSource
import com.cramsan.framework.logging.Severity

import java.util.ArrayList
import java.util.HashMap

/**
 * This fragment will retrieve the list of weapons for a player and display it.
 */
class FragmentWeaponList : BaseFragment() {

    private var profileId: String? = null
    private var profileFaction: String? = null

    private var weaponKills: ArrayList<WeaponStat>? = null
    private var weaponKilledBy: ArrayList<WeaponStat>? = null
    private var namespace: DBGCensus.Namespace? = null

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
        return inflater.inflate(R.layout.fragment_weapon_list, container, false)
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
        val listRoot = activity!!.findViewById<View>(R.id.listViewWeaponList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            /*mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterEvent) myAdapter.getItemAtPosition(myItemInt)).getImportant_character_id() });*/
        }

        this.fragmentMyWeapons.setOnCheckedChangeListener { buttonView, isChecked ->
            val listRoot = activity!!.findViewById<View>(R.id.listViewWeaponList) as ListView
            val weaponAdapter = listRoot.adapter as WeaponItemAdapter
            if (weaponAdapter != null) {
                weaponAdapter.isMyWeapons = isChecked
                listRoot.adapter = weaponAdapter
            }
        }

        this.profileId = arguments!!.getString("PARAM_0")
        this.namespace = DBGCensus.Namespace.valueOf(arguments!!.getString("PARAM_1"))
        this.profileFaction = ""
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadWeaponList(this.profileId)
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }

    /**
     * @param character_id retrieve the weapon list for a character with the given
     * character_id and displays it.
     */
    fun downloadWeaponList(character_id: String?) {
        if ("" == profileFaction) {
            val task = GetProfileFromTable()
            setCurrentTask(task)
            task.execute(this.profileId)
            return
        }

        setProgressButton(true)
        val url = dbgCensus.generateGameDataRequest(
            "characters_weapon_stat_by_faction/?" +
                    "character_id=" + character_id + "&c:join=item^show:image_path'name." + dbgCensus.currentLang.name.toLowerCase() +
                    "&c:join=vehicle^show:image_path'name." + dbgCensus.currentLang.name.toLowerCase() + "&c:limit=10000",
            namespace!!
        )!!.toString()

        val success = Listener<Weapon_list_response> { response ->
            setProgressButton(false)
            try {
                val currentTask = GenerateWeaponStats()
                setCurrentTask(currentTask)
                currentTask.execute(response)
            } catch (e: Exception) {
                metrics.log(TAG, Constants.ERROR_PARSING_RESPONE)
                eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_PARSING_RESPONE)
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val error = ErrorListener {
            setProgressButton(false)
            metrics.log(TAG, Constants.ERROR_MAKING_REQUEST)
            eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_MAKING_REQUEST)
            Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                .show()
        }
        dbgCensus.sendGsonRequest(url, Weapon_list_response::class.java, success, error, this)
    }

    /**
     * Use this task to go over the data we recieved and generate the objects that
     * we are going to pass to the adapter
     */
    private inner class GenerateWeaponStats : AsyncTask<Weapon_list_response, Int, Int>() {

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
        override fun doInBackground(vararg args: Weapon_list_response): Int? {
            val response = args[0]
            var weaponKillStats = ArrayList<WeaponStat>()
            var weaponKilledByStats = ArrayList<WeaponStat>()

            val weaponMap = HashMap<String, WeaponStat>()
            val weaponKilledMap = HashMap<String, WeaponStat>()

            var statA = 0
            var statB = 0
            var weaponName: String?
            for (weapon in response.getcharacters_weapon_stat_by_faction_list()!!) {
                val stat: WeaponStat
                val statMap: HashMap<String, WeaponStat>
                if (weapon.item_id_join_item == null && weapon.vehicle_id_join_vehicle == null) {
                    continue
                } else {
                    try {
                        if (weapon.item_id_join_item != null) {
                            weaponName = weapon.item_id_join_item!!.name!!.localizedName(dbgCensus.currentLang)
                        } else {
                            continue
                        }
                    } catch (e: Exception) {
                        continue
                    }

                }

                if (weapon.stat_name == "weapon_vehicle_kills" ||
                    weapon.stat_name == "weapon_headshots" ||
                    weapon.stat_name == "weapon_kills"
                ) {
                    statMap = weaponMap
                } else if (weapon.stat_name == "weapon_killed_by") {
                    statMap = weaponKilledMap
                } else {
                    continue
                }

                if (!statMap.containsKey(weaponName)) {
                    stat = WeaponStat()

                    if (weapon.item_id_join_item != null) {
                        stat.imagePath = weapon.item_id_join_item!!.image_path
                    } else if (weapon.vehicle_id_join_vehicle != null) {
                        stat.imagePath = weapon.vehicle_id_join_vehicle!!.image_path
                    }


                    if (weapon.vehicle_id_join_vehicle != null) {
                        stat.name = weaponName
                        stat.vehicle = weapon.vehicle_id_join_vehicle!!.name!!.localizedName(dbgCensus.currentLang)
                    } else {
                        stat.name = weaponName
                    }

                    statMap[weaponName!!] = stat
                } else {
                    stat = statMap[weaponName]!!
                }

                if (statMap === weaponKilledMap) {
                    stat.kills = weapon.value_nc +
                            weapon.value_tr +
                            weapon.value_vs

                } else {
                    if (profileFaction == Faction.VS) {
                        statA = weapon.value_tr
                        statB = weapon.value_nc
                    } else if (profileFaction == Faction.NC) {
                        statA = weapon.value_tr
                        statB = weapon.value_vs
                    } else if (profileFaction == Faction.TR) {
                        statA = weapon.value_vs
                        statB = weapon.value_nc
                    }

                    if (weapon.stat_name == "weapon_vehicle_kills") {
                        stat.vehicleKills = statA + statB
                    } else if (weapon.stat_name == "weapon_headshots") {
                        stat.headshots = statA + statB
                    } else if (weapon.stat_name == "weapon_kills") {
                        stat.kills = statA + statB
                        stat.vs = weapon.value_vs
                        stat.tr = weapon.value_tr
                        stat.nc = weapon.value_nc
                    }
                }
            }
            weaponKillStats = ArrayList(weaponMap.values)
            weaponKilledByStats = ArrayList(weaponKilledMap.values)

            java.util.Collections.sort(weaponKillStats)
            java.util.Collections.sort(weaponKilledByStats)

            for (i in weaponKillStats.indices.reversed()) {
                if (weaponKillStats[i].kills <= 0) {
                    weaponKillStats.removeAt(i)
                } else if (weaponKillStats[i].kills > 0) {
                    break
                }
            }

            weaponKills = weaponKillStats
            weaponKilledBy = weaponKilledByStats

            return weaponKilledByStats.size + weaponKilledByStats.size
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Int?) {
            if (this.isCancelled) {
                return
            }
            val listRoot = activity!!.findViewById<View>(R.id.listViewWeaponList) as ListView
            listRoot.adapter = WeaponItemAdapter(
                activity!!,
                weaponKills!!,
                weaponKilledBy!!,
                profileFaction!!,
                this@FragmentWeaponList.fragmentMyWeapons.isChecked,
                imageLoader
            )
            setProgressButton(false)
        }
    }

    /**
     * Read the profile from the database and retrieve the character data
     */
    private inner class GetProfileFromTable : AsyncTask<String, Int, CharacterProfile>() {

        private var profile_id: String? = null

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
        override fun doInBackground(vararg args: String): CharacterProfile? {
            this.profile_id = args[0]
            val data = activityContainer.data
            return data!!.getCharacter(profile_id!!)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: CharacterProfile?) {
            setProgressButton(false)
            if (result != null) {
                profileFaction = result.faction_id
                downloadWeaponList(this.profile_id)
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.toast_profile_download_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "FragmentWeaponList"
    }
}