package com.cramsan.petproject.download

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.databinding.ActivityDownloadDialogBinding

class DownloadCatalogDialogActivity : BaseActivity<DownloadCatalogViewModel, ActivityDownloadDialogBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_download_dialog
    override val logTag: String
        get() = "DownloadDialogActivity"
    override val enableDataBinding: Boolean
        get() = true
    override val toolbarViewId: Int?
        get() = null
    override val enableUp: Boolean
        get() = false
    override val titleResource: Int?
        get() = null
    override fun onBackPressed() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: DownloadCatalogViewModel by viewModels()
        model.observableIsDownloadComplete.observe(
            this,
            Observer<Boolean> { isDownloadComplete ->
                if (isDownloadComplete) {
                    finish()
                }
            }
        )
        if (model.isCatalogReady()) {
            finish()
        }
        model.downloadCatalog()
        viewModel = model
    }
}
