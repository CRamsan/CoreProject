package com.cramsan.petproject.debugmenu

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.PetProjectApplication
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class DebugMenuFragment : PreferenceFragmentCompat(), KodeinAware {

    override val kodein by lazy { (requireActivity().application as PetProjectApplication).kodein }

    private val eventLogger: EventLoggerInterface by instance()
    private val modelProvider: ModelProviderInterface by instance()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.debugmenu, rootKey)

        val testPreference: Preference? = findPreference("clearCache")
        testPreference?.setOnPreferenceClickListener {
            eventLogger.log(Severity.INFO, classTag(), "ClearCache")
            GlobalScope.async(Dispatchers.IO) {
                modelProvider.deleteAll()
            }
            true
        }

        val killPreference: Preference? = findPreference("killApp")
        killPreference?.setOnPreferenceClickListener {
            eventLogger.log(Severity.INFO, classTag(), "KillApp")
            activity?.finishAffinity()
            true
        }
    }
}