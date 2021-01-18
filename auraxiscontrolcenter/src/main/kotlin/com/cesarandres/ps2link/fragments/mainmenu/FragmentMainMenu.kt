package com.cesarandres.ps2link.fragments.mainmenu

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentMainMenuBinding
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.metrics.logMetric
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
@AndroidEntryPoint
class FragmentMainMenu : BasePS2Fragment<NoopViewModel, FragmentMainMenuBinding>() {

    override val viewModel: NoopViewModel by viewModels()

    override val logTag = "FragmentMainMenu"
    override val contentViewLayout = R.layout.fragment_main_menu

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val buttonCharacters = requireActivity().findViewById<View>(R.id.buttonCharacters) as Button
        val buttonServers = requireActivity().findViewById<View>(R.id.buttonServers) as Button
        val buttonOutfit = requireActivity().findViewById<View>(R.id.buttonOutfit) as Button
        val buttonTwitter = requireActivity().findViewById<View>(R.id.buttonTwitter) as Button
        val buttonReddit = requireActivity().findViewById<View>(R.id.buttonRedditFragment) as Button
        val buttonAbout = requireActivity().findViewById<View>(R.id.buttonAbout) as Button

        buttonCharacters.setOnClickListener {
            logMetric(logTag, "Open Profile")
        }
        buttonServers.setOnClickListener {
            logMetric(logTag, "Open Servers")
        }
        buttonOutfit.setOnClickListener {
            logMetric(logTag, "Open Outfit")
        }

        buttonTwitter.setOnClickListener {
            logMetric(logTag, "Open Twitter")
        }
        buttonReddit.setOnClickListener {
            logMetric(logTag, "Open Reddit")
        }
        buttonAbout.setOnClickListener {
            logMetric(logTag, "Open About")
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        checkPreferedButtons()
    }

    /**
     * This function will check the preferences to see if any profile or outfit
     * has been set as preferred. If any has been set then the respective button
     * will be displayed, they will be hidden otherwise.
     */
    fun checkPreferedButtons() {
        val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)

        val preferedProfileId = settings.getString("preferedProfile", "")
        val preferedProfileName = settings.getString("preferedProfileName", "")
        val buttonPreferedProfile =
            requireActivity().findViewById<View>(R.id.buttonPreferedProfile) as Button
        if (preferedProfileId != "") {
            buttonPreferedProfile.setOnClickListener {
                logMetric(logTag, "Open Preferred Profile")
                val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)
                TODO()
            }
            buttonPreferedProfile.text = preferedProfileName
            buttonPreferedProfile.visibility = View.VISIBLE
        } else {
            buttonPreferedProfile.visibility = View.GONE
        }

        val preferedOutfitId = settings.getString("preferedOutfit", "")
        val preferedOutfitName = settings.getString("preferedOutfitName", "")
        val buttonPreferedOutfit =
            requireActivity().findViewById<View>(R.id.buttonPreferedOutfit) as Button
        if (preferedOutfitId != "") {

            buttonPreferedOutfit.setOnClickListener {
                logMetric(logTag, "Open Preferred Outfit")
                val settings = requireActivity().getSharedPreferences("PREFERENCES", 0)
                TODO()
            }
            buttonPreferedOutfit.visibility = View.VISIBLE
            buttonPreferedOutfit.text = preferedOutfitName
        } else {
            buttonPreferedOutfit.visibility = View.GONE
        }
    }
}
