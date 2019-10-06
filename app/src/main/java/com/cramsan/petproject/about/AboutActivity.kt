package com.cramsan.petproject.about

import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.about_toolbar

class AboutActivity : BaseActivity() {
    override val contentViewLayout: Int
        get() = R.layout.activity_about
    override val titleResource: Int
        get() = R.string.title_activity_about
    override val toolbar: Toolbar
        get() = about_toolbar
}
