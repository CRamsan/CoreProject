package com.cesarandres.ps2link.fragments.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuCompose
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
@AndroidEntryPoint
class FragmentComposeMainMenu : BaseComposePS2Fragment<MainMenuAndroidViewModel>() {

    override val logTag = "FragmentComposeMainMenu"
    override val viewModel: MainMenuAndroidViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val preferredProfile = viewModel.preferredProfile.collectAsState(null)
        val preferredOutfit = viewModel.preferredOutfit.collectAsState(null)
        MainMenuCompose(
            preferredProfile = preferredProfile.value,
            preferredOutfit = preferredOutfit.value,
            eventHandler = viewModel,
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateUI()
    }
}
