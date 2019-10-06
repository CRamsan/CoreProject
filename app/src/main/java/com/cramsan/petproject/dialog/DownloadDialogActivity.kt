package com.cramsan.petproject.dialog

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var viewModel: DownloadCatalogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DownloadCatalogViewModel::class.java)
        viewModel.observableLoading().observe(this, Observer<Boolean> { isLoading ->
            if (!isLoading) {
                finish()
            }
        })
        if(viewModel.isCatalogReady()) {
            finish()
        }
        viewModel.downloadCatalog()
    }

}
