package com.cramsan.petproject.download

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.mainmenu.DownloadCatalogViewModel

class DownloadDialogActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_download_dialog
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = null
    override val tag: String
        get() = "DownloadDialogActivity"

    private lateinit var viewModel: DownloadCatalogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(DownloadCatalogViewModel::class.java)
        viewModel.observableLoading().observe(this, Observer<Boolean> { isLoading ->
            if (!isLoading) {
                finish()
            }
        })
        if (viewModel.isCatalogReady()) {
            finish()
        }
        viewModel.downloadCatalog()
    }

    override fun onBackPressed() = Unit
}
