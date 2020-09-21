package com.cramsan.petproject.download

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseDialogFragment
import com.cramsan.petproject.databinding.FragmentDownloadDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadCatalogDialogFragment : BaseDialogFragment<DownloadCatalogViewModel, FragmentDownloadDialogBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.fragment_download_dialog
    override val logTag: String
        get() = "DownloadDialogActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: DownloadCatalogViewModel by viewModels()
        model.observableIsDownloadComplete.observe(
            this,
            Observer<Boolean> { isDownloadComplete ->
                if (isDownloadComplete) {
                    closeDialog()
                }
            }
        )
        if (model.isCatalogReady()) {
            closeDialog()
        }
        model.downloadCatalog()
        viewModel = model
    }

    private fun closeDialog() {
        val action = DownloadCatalogDialogFragmentDirections.actionDownloadCatalogDialogFragmentPop()
        findNavController().navigate(action)
    }
}
