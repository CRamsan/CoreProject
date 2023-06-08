package com.cramsan.ps2link.appfrontend.profilepager.killlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.KillItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render the killfeed for this user.
 */
@Composable
fun KillListCompose(
    killList: ImmutableList<KillEventUIModel>,
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
                    val time = it.time ?: UnknownText()
                    KillItem(
                        modifier = Modifier.fillMaxWidth(),
                        killType = it.killType,
                        faction = it.faction,
                        attacker = it.attacker ?: UnknownText(),
                        time = time,
                        weaponName = it.weaponName ?: UnknownText(),
                        weaponImage = it.weaponImage,
                        onClick = {
                            it.characterId?.let { characterId ->
                                eventHandler.onProfileSelected(characterId, it.namespace)
                            }
                        },
                    )
                }
            }
            ErrorOverlay(isError = isError, error = UnknownErrorText())
        }
    }
}

interface KillListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

data class KillEventUIModel(
    val characterId: String?,
    val namespace: Namespace,
    val killType: KillType = KillType.UNKNOWN,
    val faction: Faction = Faction.UNKNOWN,
    val attacker: String? = null,
    val time: String? = null,
    val weaponName: String? = null,
    val weaponImage: String? = null,
)
