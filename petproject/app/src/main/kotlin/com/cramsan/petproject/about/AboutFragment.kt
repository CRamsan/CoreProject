package com.cramsan.petproject.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import com.cramsan.framework.core.BaseDatabindingFragment
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.petproject.R
import com.cramsan.petproject.databinding.FragmentAboutBinding

/**
 * Represents the About screen.
 */
class AboutFragment : BaseDatabindingFragment<NoopViewModel, FragmentAboutBinding>() {

    override val contentViewLayout: Int
        get() = R.layout.fragment_about
    override val logTag: String
        get() = "AboutActivity"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.referenceFreepik.movementMethod = LinkMovementMethod.getInstance()
    }

    override val viewModel: NoopViewModel by viewModels()
}
