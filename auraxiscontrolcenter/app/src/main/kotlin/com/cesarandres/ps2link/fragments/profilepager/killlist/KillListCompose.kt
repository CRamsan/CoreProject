package com.cesarandres.ps2link.fragments.profilepager.killlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.KillItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Render the killfeed for this user.
 */
@Composable
fun KillListCompose(
    killList: ImmutableList<KillEvent>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: KillListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(killList) {
                    val time = it.time?.let {
                        formatter.format(Date(it.toEpochMilliseconds()))
                    } ?: stringResource(R.string.text_unknown)
                    KillItem(
                        modifier = Modifier.fillMaxWidth(),
                        killType = it.killType,
                        faction = it.faction,
                        attacker = it.attacker ?: stringResource(R.string.text_unknown),
                        time = time,
                        weaponName = it.weaponName ?: stringResource(R.string.text_unknown),
                        weaponImage = it.weaponImage,
                        onClick = {
                            it.characterId?.let { characterId ->
                                eventHandler.onProfileSelected(characterId, it.namespace)
                            }
                        },
                    )
                }
            }
            ErrorOverlay(isError = isError, error = stringResource(id = R.string.text_unkown_error))
        }
    }
}

@MainThread
interface KillListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

var formatter = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())

@Preview
@Composable
fun Preview() {
    KillListCompose(
        killList = persistentListOf(),
        isLoading = true,
        isError = false,
        eventHandler = object : KillListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
