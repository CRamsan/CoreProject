package com.cesarandres.ps2link.fragments.about

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
@AndroidEntryPoint
class FragmentComposeAbout : BaseComposePS2Fragment<AboutViewModel>() {

    override val logTag = "FragmentComposeAbout"
    override val viewModel: AboutViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        AboutCompose(
            eventHandler = viewModel,
        )
    }
}
