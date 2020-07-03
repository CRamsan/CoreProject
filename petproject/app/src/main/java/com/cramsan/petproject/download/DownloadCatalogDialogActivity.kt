package com.cramsan.petproject.download

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity

class DownloadCatalogDialogActivity : BaseActivity<DownloadCatalogViewModel>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_download_dialog
    override val tag: String
        get() = "DownloadDialogActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: DownloadCatalogViewModel by viewModels()
        model.observableIsDownloadComplete.observe(this, Observer<Boolean> { isDownloadComplete ->
            if (isDownloadComplete) {
                finish()
            }
        })
        if (model.isCatalogReady()) {
            finish()
        }
        model.downloadCatalog()
        viewModel = model
    }

    override fun onBackPressed() = Unit
}
