package com.cramsan.petproject.debugmenu

import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.base.NoopViewModel
import com.cramsan.petproject.databinding.ActivityDebugMenuBinding

class DebugMenuActivity : BaseActivity<NoopViewModel, ActivityDebugMenuBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_debug_menu
    override val titleResource: Int
        get() = R.string.title_activity_debug
    override val enableDataBinding: Boolean
        get() = true
    override val toolbarViewId: Int?
        get() = R.id.debug_menu_toolbar
    override val logTag: String
        get() = "DebugMenuActivity"
    override val enableUp: Boolean
        get() = true
}
