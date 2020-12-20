package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentAddProfileBinding
import com.cesarandres.ps2link.dbg.view.LoadingItemAdapter
import com.cesarandres.ps2link.dbg.view.ProfileItemAdapter
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * This fragment will show the user with a field and a button to search for
 * profiles. The only requirement is that the name needs to be at least three
 * characters long.
 */
class FragmentAddProfile : BasePS2Fragment<NoopViewModel, FragmentAddProfileBinding>(), SourceSelectionChangedListener {

    private var lastUsedNamespace: Namespace? = null
    private var selectionButton: ButtonSelectSource? = null

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val buttonCharacters =
            requireActivity().findViewById<View>(R.id.imageButtonSearchProfile) as ImageButton
        buttonCharacters.setOnClickListener {
            metrics.log(TAG, "Search Profile")
            downloadProfiles()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    /**
     * This method will retrieve profiles based on the criteria given by the
     * user in the text fields. The user needs to provide a name to start a
     * search. If a value is provided but it is less than three characters long,
     * then the user will see a toast asking to provide more information.
     */
    private fun downloadProfiles() {
        val searchField = requireView().findViewById<View>(R.id.fieldSearchProfile) as EditText
        this.lastUsedNamespace = selectionButton!!.namespace
        if (searchField.text.toString().length < 3) {
            Toast.makeText(activity, R.string.text_profile_name_too_short, Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Set the loading adapter
        val listRoot = requireView().findViewById<View>(R.id.listFoundProfiles) as ListView
        listRoot.onItemClickListener = null
        listRoot.adapter = LoadingItemAdapter(this!!.requireActivity())

        viewLifecycleOwner.lifecycleScope.launch {
            val profiles = withContext(Dispatchers.IO) { dbgCensus.getProfiles(searchField.text.toString().toLowerCase(Locale.getDefault()), selectionButton!!.namespace, CensusLang.EN) }
            val listRoot = requireView().findViewById<View>(R.id.listFoundProfiles) as ListView
            listRoot.adapter = ProfileItemAdapter(
                requireActivity(),
                profiles!!,
                false
            )
        }
    }

    override fun onSourceSelectionChanged(selectedNamespace: Namespace) {
        downloadProfiles()
    }

    companion object {
        const val TAG = "FragmentAddProfile"
    }

    override val logTag = "FragmentAddProfile"
    override val contentViewLayout = R.layout.fragment_add_profile
}
