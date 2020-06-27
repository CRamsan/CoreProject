package com.cramsan.petproject.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.appcompat.widget.Toolbar
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.base.NoopViewModel
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : BaseActivity<NoopViewModel>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_about
    override val titleResource: Int
        get() = R.string.title_activity_about
    override val toolbar: Toolbar
        get() = about_toolbar
    override val tag: String
        get() = "AboutActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reference_freepik.movementMethod = LinkMovementMethod.getInstance()
    }
}
