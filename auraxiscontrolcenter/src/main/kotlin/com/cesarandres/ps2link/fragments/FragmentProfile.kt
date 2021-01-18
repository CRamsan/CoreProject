package com.cesarandres.ps2link.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentOutfitBinding
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.metrics.logMetric
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile
import com.cramsan.ps2link.appcore.dbg.content.Faction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

/**
 * This fragment will read a profile from the database and display it to the
 * user. It will then try to update the data by doing a query to the API
 */
class FragmentProfile : BasePS2Fragment<NoopViewModel, FragmentOutfitBinding>() {

    override val viewModel: NoopViewModel by viewModels()
    private var isCached: Boolean = false
    private var profile: CharacterProfile? = null
    private var profileId: String = ""
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
        this.profileId = requireArguments().getString("PARAM_0", "")
        this.namespace = Namespace.valueOf(requireArguments().getString("PARAM_1", ""))
        logMetric(TAG, "Loading Profile", mapOf("PROFILE" to this.profileId, "NAMESPACE" to this.namespace!!.name))
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
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    /**
     * @param character Character that contains all the data to populate the UI
     */
    private fun updateUI(character: CharacterProfile) {
        logI(TAG, "Updating UI")
        try {
            if (this.view != null) {
                val faction =
                    requireActivity().findViewById<View>(R.id.imageViewProfileFaction) as ImageView
                if (character.faction_id == Faction.VS) {
                    faction.setImageResource(R.drawable.icon_faction_vs)
                } else if (character.faction_id == Faction.NC) {
                    faction.setImageResource(R.drawable.icon_faction_nc)
                } else if (character.faction_id == Faction.TR) {
                    faction.setImageResource(R.drawable.icon_faction_tr)
                }

                val initialBR = requireActivity().findViewById<View>(R.id.textViewCurrentRank) as TextView
                initialBR.text = Integer.toString(character.battle_rank!!.value)
                initialBR.setTextColor(Color.BLACK)

                val nextBR = requireActivity().findViewById<View>(R.id.textViewNextRank) as TextView
                nextBR.text = Integer.toString(character.battle_rank!!.value + 1)
                nextBR.setTextColor(Color.BLACK)

                val progressBR = character.battle_rank!!.percent_to_next
                (requireActivity().findViewById<View>(R.id.progressBarProfileBRProgress) as ProgressBar).progress =
                    progressBR

                val progressCerts = java.lang.Float.parseFloat(character.certs!!.percent_to_next!!)
                (requireActivity().findViewById<View>(R.id.progressBarProfileCertsProgress) as ProgressBar).progress =
                    (progressCerts * 100).toInt()
                val certs =
                    requireActivity().findViewById<View>(R.id.textViewProfileCertsValue) as TextView
                certs.text = character.certs!!.available_points

                val loginStatus =
                    requireActivity().findViewById<View>(R.id.TextViewProfileLoginStatusText) as TextView
                var onlineStatusText = requireActivity().resources.getString(R.string.text_unknown)
                if (character.online_status == 0) {
                    onlineStatusText = requireActivity().resources.getString(R.string.text_offline_caps)
                    loginStatus.text = onlineStatusText
                    loginStatus.setTextColor(Color.RED)
                } else {
                    onlineStatusText = requireActivity().resources.getString(R.string.text_online_caps)
                    loginStatus.text = onlineStatusText
                    loginStatus.setTextColor(Color.GREEN)
                }

                (requireActivity().findViewById<View>(R.id.textViewProfileMinutesPlayed) as TextView).text =
                    Integer.toString(
                        Integer.parseInt(
                            character.times!!
                                .minutes_played!!
                        ) / 60
                    )

                val p = PrettyTime()
                val lastLogin =
                    p.format(Date(java.lang.Long.parseLong(character.times!!.last_login!!) * 1000))

                (requireActivity().findViewById<View>(R.id.textViewProfileLastLogin) as TextView).text =
                    lastLogin

                val outfitButton =
                    requireActivity().findViewById<View>(R.id.buttonProfileToOutfit) as Button

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
                        logMetric(TAG, "Open Outfit")
                    }
                }

                if (character.server != null) {
                    (requireActivity().findViewById<View>(R.id.textViewServerText) as TextView).text =
                        character.server!!.name!!.localizedName(CensusLang.EN)
                } else {
                    (requireActivity().findViewById<View>(R.id.textViewServerText) as TextView).text =
                        requireActivity().resources.getString(R.string.text_unknown)
                }
            }

            val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)
            val preferedProfileId = settings.getString("preferedProfile", "")
        } catch (e: NullPointerException) {
            logMetric(TAG, "NPE when updating the UI")
            logE(TAG, "Null Pointer while trying to set character data on UI")
        }
        logI(TAG, "UI was updated")
    }

    /**
     * @param character_id Character id of the character that wants to be download
     */
    fun downloadProfiles(character_id: String?) {
        logI(TAG, "Downloading Profile")
        this

            .lifecycleScope.launch {
                val profile = withContext(Dispatchers.IO) { dbgCensus.getProfile(character_id!!, namespace!!, CensusLang.EN) }

                logI(TAG, "Profile Downloaded")
                // profile!!.namespace = namespace!!
                profile!!.isCached = isCached
                updateUI(profile!!)
            }
    }

    companion object {
        const val TAG = "FragmentProfile"
    }

    override val logTag: String
        get() = TODO("Not yet implemented")
    override val contentViewLayout: Int
        get() = TODO("Not yet implemented")
}
