package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentAddOutfitBinding
import com.cesarandres.ps2link.dbg.view.LoadingItemAdapter
import com.cesarandres.ps2link.dbg.view.OutfitItemAdapter
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.metrics.logMetric
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * This fragment will show the user a field to search for outfits based on their
 * tag or name. The tag has the limitation that it has to be at least three
 * characters long. When an outfit is found, it's content is cached into the
 * database.
 */
class FragmentAddOutfit : BasePS2Fragment<NoopViewModel, FragmentAddOutfitBinding>() {

    override val viewModel: NoopViewModel by viewModels()
    // private lateinit var selectionButton: ButtonSelectSource
    private var lastUsedNamespace: Namespace? = null

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val buttonOutfits =
            requireActivity().findViewById<View>(R.id.imageButtonSearchOutfit) as ImageButton
        buttonOutfits.setOnClickListener {
            logMetric(TAG, "Search")
            downloadOutfits()
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * This method will retrieve the outfits based on the criteria given by the
     * user in the text fields. The user needs to provide a name or a tag or
     * both to start a search. If a value is provided but it is less than three
     * characters long, then the user will see a toast asking to provide more
     * information.
     */
    fun downloadOutfits() {
        this.lastUsedNamespace = Namespace.UNDETERMINED // Namespace.UNDETERMINED.toNetworkModel()

        val searchField = requireActivity().findViewById<View>(R.id.fieldSearchOutfit) as EditText
        val searchTagField = requireActivity().findViewById<View>(R.id.fieldSearchTag) as EditText
        val outfitName = searchField.text.toString().toLowerCase(Locale.getDefault())
        val outfitTag = searchTagField.text.toString().toLowerCase(Locale.getDefault())

        // Check if the input values are valid
        if (!outfitTag.isEmpty() && outfitTag.length < 3) {
            Toast.makeText(activity, R.string.text_tag_too_short, Toast.LENGTH_SHORT).show()
        }
        if (!outfitName.isEmpty() && outfitName.length < 3) {
            Toast.makeText(activity, R.string.text_outfit_name_too_short, Toast.LENGTH_SHORT).show()
        }
        if (outfitName.length < 3 && outfitTag.length < 3) {
            // Clear the loading adapter
            return
        }

        val listRoot = requireActivity().findViewById<View>(R.id.listFoundOutfits) as ListView
        listRoot.onItemClickListener = null
        // Set the loading adapter while searching
        listRoot.adapter = LoadingItemAdapter(requireActivity())

        viewLifecycleOwner.lifecycleScope.launch {
            val outfitList = withContext(Dispatchers.IO) { dbgCensus.getOutfitList(outfitTag, outfitName, com.cramsan.ps2link.network.models.Namespace.UNDETERMINED, CensusLang.EN) }

            val listRoot = requireActivity().findViewById<View>(R.id.listFoundOutfits) as ListView
            listRoot.adapter = OutfitItemAdapter(requireActivity(), outfitList!!)

            listRoot.onItemClickListener =
                OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
                    TODO()
                }

            // Add the new outfits to the local cache
            listRoot.isTextFilterEnabled = true
        }
    }

    fun onSourceSelectionChanged(selectedNamespace: Namespace) {
        downloadOutfits()
    }

    companion object {
        const val TAG = "FragmentAddOutfit"
    }

    override val logTag = "FragmentAddOutfit"
    override val contentViewLayout = R.layout.fragment_add_outfit
}
