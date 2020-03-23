package com.cramsan.petproject.debugmenu

import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_debug_menu.debug_menu_toolbar

class DebugMenuActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_debug_menu
    override val titleResource: Int
        get() = R.string.title_activity_debug
    override val toolbar: Toolbar
        get() = debug_menu_toolbar
}
