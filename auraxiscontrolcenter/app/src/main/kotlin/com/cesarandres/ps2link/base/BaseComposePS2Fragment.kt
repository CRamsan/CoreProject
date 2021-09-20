package com.cesarandres.ps2link.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.NavOptions
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
import com.cesarandres.ps2link.fragments.OpenUrl
import com.cesarandres.ps2link.toMetadataMap
import com.cramsan.framework.core.BaseEvent
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.ComposeBaseFragment
import com.cramsan.framework.logging.logW
import com.cramsan.framework.userevents.logEvent
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
                logEvent(logTag, event.javaClass.simpleName, event.args.toBundle().toMetadataMap())
                findNavController().navigate(R.id.fragmentProfilePager, event.args.toBundle(), navigationOptions)
            }
            is OpenOutfit -> {
                logEvent(logTag, event.javaClass.simpleName, event.args.toBundle().toMetadataMap())
                findNavController().navigate(R.id.fragmentOutfitPager, event.args.toBundle(), navigationOptions)
            }
            is OpenProfileList -> {
                logEvent(logTag, event.javaClass.simpleName)
                findNavController().navigate(R.id.fragmentProfileList, null, navigationOptions)
            }
            is OpenOutfitList -> {
                logEvent(logTag, event.javaClass.simpleName)
                findNavController().navigate(R.id.fragmentOutfitList, null, navigationOptions)
            }
            is OpenServerList -> {
                logEvent(logTag, event.javaClass.simpleName)
                findNavController().navigate(R.id.fragmentServerList, null, navigationOptions)
            }
            is OpenTwitter -> {
                logEvent(logTag, event.javaClass.simpleName)
                findNavController().navigate(R.id.fragmentTwitter, null, navigationOptions)
            }
            is OpenReddit -> {
                logEvent(logTag, event.javaClass.simpleName)
                findNavController().navigate(R.id.fragmentReddit, null, navigationOptions)
            }
            is OpenAbout -> {
                logEvent(logTag, event.javaClass.simpleName)
                findNavController().navigate(R.id.fragmentAbout, null, navigationOptions)
            }
            is OpenUrl -> {
                logEvent(logTag, event.javaClass.simpleName)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.url)
                startActivity(intent)
            }
            else -> {
                logW(logTag, "Unhandled event: $event")
            }
        }
    }

    val navigationOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .build()

    @Composable
    abstract fun CreateComposeContent()

    @CallSuper
    override fun onStart() {
        super.onStart()
        logEvent(logTag, EVENT_CREATED)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        logEvent(logTag, EVENT_DISPLAYED)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        logEvent(logTag, EVENT_HIDDEN)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        logEvent(logTag, EVENT_DESTROYED)
    }

    companion object {
        private const val EVENT_CREATED = "Created"
        private const val EVENT_DISPLAYED = "Displayed"
        private const val EVENT_HIDDEN = "Hidden"
        private const val EVENT_DESTROYED = "Destroyed"
    }
}
