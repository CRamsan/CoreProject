package com.cramsan.petproject.about

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.petproject.base.BaseComposePetProjectFragment

/**
 * Represents the About screen.
 */
class AboutFragment : BaseComposePetProjectFragment<NoopViewModel>() {

    override val logTag: String
        get() = "AboutFragment"

    override val viewModel: NoopViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        AboutScreen()
    }
}
