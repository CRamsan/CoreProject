package com.cramsan.petproject.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import com.cramsan.petproject.base.NoopViewModel
import com.cramsan.petproject.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity<NoopViewModel, ActivityAboutBinding>() {
    override val contentViewLayout: Int
        get() = R.layout.activity_about
    override val toolbarViewId: Int?
        get() = R.id.about_toolbar
    override val enableDataBinding: Boolean
        get() = true
    override val titleResource: Int
        get() = R.string.title_activity_about
    override val enableUp: Boolean
        get() = true
    override val logTag: String
        get() = "AboutActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding.referenceFreepik.movementMethod = LinkMovementMethod.getInstance()
    }
}
