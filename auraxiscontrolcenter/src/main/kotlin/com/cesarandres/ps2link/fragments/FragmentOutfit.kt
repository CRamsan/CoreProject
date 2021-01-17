package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentOutfitBinding
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.logging.logE
import com.cramsan.framework.metrics.logMetric
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
class FragmentOutfit : BasePS2Fragment<NoopViewModel, FragmentOutfitBinding>() {

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
        this.outfitId = requireArguments().getString("PARAM_0")
        this.namespace = Namespace.valueOf(requireArguments().getString("PARAM_1", ""))
    }

    /**
     * @param character Character that contains all the data to populate the UI
     */
    private fun updateUI(outfit: Outfit) {
        try {
            if (this.view != null) {
                val outfitName =
                    requireActivity().findViewById<View>(R.id.textViewFragmentOutfitName) as TextView
                outfitName.text = "[" + outfit.alias + "] " + outfit.name

                val outfitSize = requireActivity().findViewById<View>(R.id.textViewMembersText) as TextView
                outfitSize.text = Integer.toString(outfit.member_count)

                val outfitCreation =
                    requireActivity().findViewById<View>(R.id.TextViewOutfitCreationText) as TextView
                val date = Date(java.lang.Long.parseLong(outfit.time_created!!) * 1000)

                val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
                outfitCreation.text = df.format(date)

                val leaderButton =
                    requireActivity().findViewById<View>(R.id.buttonOutfitToLeader) as Button

                if (outfit.leader != null) {
                    val faction =
                        requireActivity().findViewById<View>(R.id.imageViewOutfitFaction) as ImageView
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
                        logMetric(TAG, "Open Outfit Leader")
                        TODO()
                    }
                }
            }

            val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)
            val preferedOutfitId = settings.getString("preferedOutfit", "")
        } catch (e: NullPointerException) {
            logMetric(TAG, "NPE when updating the UI")
            logE(TAG, "Null Pointer while trying to set character data on UI")
        }
    }

    /**
     * @param character_id Character id of the character that wants to be download
     */
    fun downloadOutfit(outfitId: String?) {
        lifecycleScope.launch {
            val outfit = dbgCensus.getOutfit(outfitId!!, namespace!!, CensusLang.EN)

            updateUI(outfit!!)
        }
    }

    companion object {
        const val TAG = "FragmentOutfit"
    }

    override val logTag = "FragmentOutfit"
    override val contentViewLayout = R.layout.fragment_outfit
}
