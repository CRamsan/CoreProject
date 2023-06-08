package com.cramsan.ps2link.appfrontend.profilepager.weaponlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.WeaponItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render the weapons used by a character and it's stats.
 */
@Composable
fun WeaponListCompose(
    faction: Faction,
    weaponList: ImmutableList<WeaponItem>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: WeaponListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(weaponList) { weapon ->
                    val totalKills = weapon.statMapping[WeaponEventType.KILLS]
                        ?.stats?.values?.filterNotNull()?.sum()
                    val totalVehiclesDestroyed = weapon.statMapping[WeaponEventType.VEHICLE_KILLS]
                        ?.stats?.values?.filterNotNull()?.sum()
                    val totalHeadshotKills = weapon.statMapping[WeaponEventType.HEADSHOT_KILLS]
                        ?.stats?.values?.filterNotNull()?.sum()
                    WeaponItem(
                        modifier = Modifier.fillMaxWidth(),
                        faction = faction,
                        weaponImage = weapon.weaponImage,
                        weaponName = weapon.weaponName ?: weapon.vehicleName ?: UnknownText(),
                        medalType = weapon.medalType ?: MedalType.NONE,
                        totalKills = KillsText(totalKills),
                        totalVehiclesDestroyed = VehiclesDestroyedText(totalVehiclesDestroyed),
                        totalHeadshotKills = HeadshotsText(totalHeadshotKills),
                        NCKills = weapon.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.NC)?.let {
                            NCCountText(it)
                        },
                        VSKills = weapon.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.VS)?.let {
                            VSCountText(it)
                        },
                        TRKills = weapon.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.TR)?.let {
                            TRCountText(it)
                        },
                    )
                }
            }
            ErrorOverlay(isError = isError, error = UnknownErrorText())
        }
    }
}

interface WeaponListEventHandler {
    fun onRefreshRequested()
}

@Composable
expect fun KillsText(count: Long?): String

@Composable
expect fun VehiclesDestroyedText(count: Long?): String

@Composable
expect fun HeadshotsText(count: Long?): String

@Composable
expect fun NCCountText(count: Long): String

@Composable
expect fun TRCountText(count: Long): String

@Composable
expect fun VSCountText(count: Long): String
