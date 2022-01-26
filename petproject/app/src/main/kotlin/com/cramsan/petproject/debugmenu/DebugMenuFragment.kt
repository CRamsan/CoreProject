package com.cramsan.petproject.debugmenu

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cramsan.petproject.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Debug menu screen. This screen will only be accessible on debug builds.
 */
@AndroidEntryPoint
class DebugMenuFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: DebugMenuViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val model: DebugMenuViewModel by viewModels()
        viewModel = model
        setPreferencesFromResource(R.xml.debugmenu, rootKey)

        val testPreference: Preference? = findPreference("clearCache")
        testPreference?.setOnPreferenceClickListener {
            viewModel.cleanCache()
            true
        }

        val killPreference: Preference? = findPreference("killApp")
        killPreference?.setOnPreferenceClickListener {
            viewModel.killApp(requireActivity())
            true
        }
    }
}
