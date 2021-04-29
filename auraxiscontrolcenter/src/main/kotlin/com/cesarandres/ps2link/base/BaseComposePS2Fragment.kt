package com.cesarandres.ps2link.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.fragments.OpenAbout
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.OpenOutfitList
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.fragments.OpenProfileList
import com.cesarandres.ps2link.fragments.OpenReddit
import com.cesarandres.ps2link.fragments.OpenServerList
import com.cesarandres.ps2link.fragments.OpenTwitter
import com.cramsan.framework.core.BaseEvent
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.ComposeBaseFragment
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.ui.theme.PS2Theme
import javax.inject.Inject

/**
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BaseComposePS2Fragment<VM : BaseViewModel> : ComposeBaseFragment<VM>() {

    @Inject
    protected lateinit var dbgCensus: DBGServiceClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return ComposeView(requireContext()).apply {
            setContent {
                PS2Theme {
                    CreateComposeContent()
                }
            }
        }
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenProfile -> {
                findNavController().navigate(R.id.fragmentProfilePager, event.args.toBundle())
            }
            is OpenOutfit -> {
                findNavController().navigate(R.id.fragmentOutfitPager, event.args.toBundle())
            }
            is OpenProfileList -> {
                findNavController().navigate(R.id.fragmentProfileList)
            }
            is OpenOutfitList -> {
                findNavController().navigate(R.id.fragmentOutfitList)
            }
            is OpenServerList -> {
                findNavController().navigate(R.id.fragmentServerList)
            }
            is OpenTwitter -> {
                findNavController().navigate(R.id.fragmentTwitter)
            }
            is OpenReddit -> {
                findNavController().navigate(R.id.fragmentReddit)
            }
            is OpenAbout -> {
                findNavController().navigate(R.id.fragmentAbout)
            }
        }
    }

    @Composable
    abstract fun CreateComposeContent()
}
