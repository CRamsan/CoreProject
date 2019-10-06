package com.cramsan.petproject.mainmenu

import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_menu.main_menu_toolbar

class MainMenuActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_main_menu
    override val titleResource: Int?
        get() = R.string.app_name
    override val toolbar: Toolbar?
        get() = main_menu_toolbar
    override val enableUp: Boolean
        get() = false
}
