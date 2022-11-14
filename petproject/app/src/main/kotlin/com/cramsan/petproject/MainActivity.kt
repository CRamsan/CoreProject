package com.cramsan.petproject

import androidx.activity.viewModels
import com.cramsan.framework.core.BaseViewActivity
import com.cramsan.framework.core.NoopViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The activity that manages all internal fragments.
 */
@AndroidEntryPoint
class MainActivity : BaseViewActivity<NoopViewModel>() {

    override val contentViewLayout = R.layout.activity_main_menu
    override val toolbarViewId: Int? = null
    override val logTag = "MainActivity"
    override val viewModel: NoopViewModel by viewModels()
}
