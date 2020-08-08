package com.cramsan.petproject.debugmenu

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cramsan.petproject.R
import org.kodein.di.DIAware
import org.kodein.di.android.x.di

class DebugMenuFragment : PreferenceFragmentCompat(), DIAware {

    override val di by di()

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
