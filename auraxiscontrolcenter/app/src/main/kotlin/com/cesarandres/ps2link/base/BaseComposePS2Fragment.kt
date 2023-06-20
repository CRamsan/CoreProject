package com.cesarandres.ps2link.base

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.fragments.outfitpager.FragmentOutfitPagerArgs
import com.cesarandres.ps2link.fragments.profilepager.FragmentProfilePagerArgs
import com.cesarandres.ps2link.toMetadataMap
import com.cramsan.framework.core.BaseEvent
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.compose.ComposeBaseFragment
import com.cramsan.framework.logging.logW
import com.cramsan.framework.userevents.logEvent
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenAbout
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenOutfit
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenOutfitList
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenProfile
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenProfileList
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenReddit
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenServerList
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenTwitter
import com.cramsan.ps2link.appfrontend.BasePS2Event.OpenUrl
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

    @Composable
    override fun ApplyTheme(content: @Composable () -> Unit) {
        PS2Theme {
            content()
        }
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenProfile -> {
                val args = FragmentProfilePagerArgs(event.characterId, event.namespace)
                logEvent(logTag, event.javaClass.simpleName, args.toBundle().toMetadataMap())
                findNavController().navigate(R.id.fragmentProfilePager, args.toBundle(), navigationOptions)
            }
            is OpenOutfit -> {
                val args = FragmentOutfitPagerArgs(event.outfitId, event.namespace)
                logEvent(logTag, event.javaClass.simpleName, args.toBundle().toMetadataMap())
                findNavController().navigate(R.id.fragmentOutfitPager, args.toBundle(), navigationOptions)
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
        .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
        .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
        .build()
}
