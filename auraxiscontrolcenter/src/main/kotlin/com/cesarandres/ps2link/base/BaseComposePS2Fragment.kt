package com.cesarandres.ps2link.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
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

    @Composable
    abstract fun CreateComposeContent()
}
