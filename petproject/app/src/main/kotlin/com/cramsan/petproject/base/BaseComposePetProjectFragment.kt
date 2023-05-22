package com.cramsan.petproject.base

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptions
import com.cramsan.framework.core.BaseEvent
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.compose.ComposeBaseFragment
import com.cramsan.framework.logging.logW
import com.cramsan.framework.userevents.logEvent
import com.cramsan.petproject.R
import com.cramsan.ps2link.ui.theme.PetProjectTheme

/**
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BaseComposePetProjectFragment<VM : BaseViewModel> : ComposeBaseFragment<VM>() {

    @Composable
    override fun ApplyTheme(content: @Composable () -> Unit) {
        PetProjectTheme {
            content()
        }
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
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
