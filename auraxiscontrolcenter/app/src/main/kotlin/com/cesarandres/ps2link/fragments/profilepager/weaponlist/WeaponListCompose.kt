package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import android.net.Uri
import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.WeaponItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
                items(weaponList) {
                    val totalKills = it.statMapping[WeaponEventType.KILLS]
                        ?.stats?.values?.filterNotNull()?.sum()
                    val totalVehiclesDestroyed = it.statMapping[WeaponEventType.VEHICLE_KILLS]
                        ?.stats?.values?.filterNotNull()?.sum()
                    val totalHeadshotKills = it.statMapping[WeaponEventType.HEADSHOT_KILLS]
                        ?.stats?.values?.filterNotNull()?.sum()
                    WeaponItem(
                        modifier = Modifier.fillMaxWidth(),
                        faction = faction,
                        weaponImage = it.weaponImage,
                        weaponName = it.weaponName ?: it.vehicleName ?: stringResource(R.string.text_unknown),
                        medalType = it.medalType ?: MedalType.NONE,
                        totalKills = stringResource(R.string.text_kills, totalKills ?: 0),
                        totalVehiclesDestroyed = stringResource(R.string.text_vehicle_kills_, totalVehiclesDestroyed ?: 0),
                        totalHeadshotKills = stringResource(R.string.text_headshots_, totalHeadshotKills ?: 0),
                        NCKills = it.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.NC)?.let {
                            stringResource(R.string.text_nc_, it)
                        },
                        VSKills = it.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.VS)?.let {
                            stringResource(R.string.text_vs_, it)

                        },
                        TRKills = it.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.TR)?.let {
                            stringResource(R.string.text_tr_, it)
                        },
                    )
                }
            }
            ErrorOverlay(isError = isError, error = stringResource(id = R.string.text_unkown_error))
        }
    }
}

@MainThread
interface WeaponListEventHandler {
    fun onRefreshRequested()
}

@Preview
@Composable
fun Preview() {
    WeaponListCompose(
        weaponList = persistentListOf(),
        faction = Faction.TR,
        isLoading = true,
        isError = false,
        eventHandler = object : WeaponListEventHandler {
            override fun onRefreshRequested() = Unit
        },
    )
}
