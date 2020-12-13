package com.cramsan.petproject.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.cramsan.framework.core.BaseFragment
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.petproject.R
import com.cramsan.petproject.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment<NoopViewModel, FragmentAboutBinding>() {

    override val contentViewLayout: Int
        get() = R.layout.fragment_about
    override val logTag: String
        get() = "AboutActivity"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataBinding.referenceFreepik.movementMethod = LinkMovementMethod.getInstance()
    }
}
