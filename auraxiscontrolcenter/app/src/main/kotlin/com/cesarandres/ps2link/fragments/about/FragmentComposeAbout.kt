package com.cesarandres.ps2link.fragments.about

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.appfrontend.about.AboutCompose
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
@AndroidEntryPoint
class FragmentComposeAbout : BaseComposePS2Fragment<AboutAndroidViewModel>() {

    override val logTag = "FragmentComposeAbout"
    override val viewModel: AboutAndroidViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        AboutCompose(
            eventHandler = viewModel,
        )
    }
}
