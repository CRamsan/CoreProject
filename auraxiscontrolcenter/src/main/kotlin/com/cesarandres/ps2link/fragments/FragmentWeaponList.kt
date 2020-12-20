package com.cesarandres.ps2link.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentWeaponListBinding
import com.cesarandres.ps2link.dbg.view.WeaponItemAdapter
import com.cesarandres.ps2link.module.Constants
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.logging.Severity
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.Faction
import com.cramsan.ps2link.appcore.dbg.content.item.Weapon
import com.cramsan.ps2link.appcore.dbg.content.item.WeaponStat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.HashMap

/**
 * This fragment will retrieve the list of weapons for a player and display it.
 */
class FragmentWeaponList : BasePS2Fragment<NoopViewModel, FragmentWeaponListBinding>() {

    private var profileId: String? = null
    private var profileFaction: String? = null

    private var weaponKills: List<WeaponStat>? = null
    private var weaponKilledBy: List<WeaponStat>? = null
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
        val listRoot = requireActivity().findViewById<View>(R.id.listViewWeaponList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            /*mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterEvent) myAdapter.getItemAtPosition(myItemInt)).getImportant_character_id() });*/
        }

        this.profileId = requireArguments().getString("PARAM_0")
        this.namespace = Namespace.valueOf(requireArguments().getString("PARAM_1", ""))
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

        viewLifecycleOwner.lifecycleScope.launch {

            val weaponListResponse = withContext(Dispatchers.IO) { dbgCensus.getWeaponList(character_id, namespace!!, CensusLang.EN) }

            if (weaponListResponse != null) {

                val currentTask = GenerateWeaponStats()
                currentTask.execute(weaponListResponse)
            } else {

                eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_MAKING_REQUEST)
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Use this task to go over the data we recieved and generate the objects that
     * we are going to pass to the adapter
     */
    private inner class GenerateWeaponStats : AsyncTask<List<Weapon>, Int, Int>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg args: List<Weapon>): Int? {
            val response = args[0]
            var weaponKillStats = ArrayList<WeaponStat>()
            var weaponKilledByStats = ArrayList<WeaponStat>()

            val weaponMap = HashMap<String, WeaponStat>()
            val weaponKilledMap = HashMap<String, WeaponStat>()

            var statA = 0
            var statB = 0
            var weaponName: String?

            try {

                val responseList = response
                if (responseList == null) {
                    weaponKills = emptyList()
                    weaponKilledBy = emptyList()
                    return 0
                }

                for (weapon in responseList) {
                    val stat: WeaponStat
                    val statMap: HashMap<String, WeaponStat>
                    if (weapon.item_id_join_item == null && weapon.vehicle_id_join_vehicle == null) {
                        continue
                    } else {
                        try {
                            if (weapon.item_id_join_item != null) {
                                weaponName = weapon.item_id_join_item!!.name!!.localizedName(CensusLang.EN)
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
                            stat.vehicle = weapon.vehicle_id_join_vehicle!!.name!!.localizedName(CensusLang.EN)
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

                // TODO: Reenable this feature
                /*
                java.util.Collections.sort(weaponKillStats)
                java.util.Collections.sort(weaponKilledByStats)
                 */

                for (i in weaponKillStats.indices.reversed()) {
                    if (weaponKillStats[i].kills <= 0) {
                        weaponKillStats.removeAt(i)
                    } else if (weaponKillStats[i].kills > 0) {
                        break
                    }
                }

                weaponKills = weaponKillStats
                weaponKilledBy = weaponKilledByStats
            } catch (e: Exception) {
                eventLogger.log(Severity.ERROR, TAG, Constants.ERROR_PARSING_RESPONE)
                return -1
            }
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
            if (requireActivity().isFinishing || requireActivity().isDestroyed) {
                return
            }

            val listRoot = requireActivity().findViewById<ListView>(R.id.listViewWeaponList)
            if (listRoot == null) {
                return
            }

            if (result == -1) {
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
                return
            } else {
                listRoot.adapter = WeaponItemAdapter(
                    requireActivity(),
                    weaponKills!!,
                    weaponKilledBy!!,
                    profileFaction!!,
                    true
                )
            }
        }
    }

    companion object {
        const val TAG = "FragmentWeaponList"
    }

    override val logTag = "FragmentWeaponList"
    override val contentViewLayout = R.layout.fragment_weapon_list
}
