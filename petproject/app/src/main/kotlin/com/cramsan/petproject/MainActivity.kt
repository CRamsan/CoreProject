package com.cramsan.petproject

import androidx.activity.viewModels
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.petproject.base.BaseNavActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * The activity that manages all internal fragments.
 */
@AndroidEntryPoint
class MainActivity : BaseNavActivity<NoopViewModel>() {

    override val contentViewLayout: Int
        get() = R.layout.activity_main_menu
    override val toolbarViewId: Int
        get() = R.id.main_menu_toolbar
    override val logTag: String
        get() = "MainMenuActivity"
    override val viewModel: NoopViewModel by viewModels()
}
