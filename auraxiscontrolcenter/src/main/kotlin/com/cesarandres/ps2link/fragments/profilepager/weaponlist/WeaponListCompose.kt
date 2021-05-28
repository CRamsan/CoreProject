package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.items.WeaponItem

@Composable
fun WeaponListCompose(
    faction: Faction,
    weaponList: List<WeaponItem>,
    isLoading: Boolean,
    isError: Boolean,
) {
    FrameBottom {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(weaponList) {
                    val totalKills = it.statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
                    val totalVehiclesDestroyed = it.statMapping[WeaponEventType.VEHICLE_KILLS]?.stats?.values?.filterNotNull()?.sum()
                    val totalHeadshotKills = it.statMapping[WeaponEventType.HEADSHOT_KILLS]?.stats?.values?.filterNotNull()?.sum()
                    WeaponItem(
                        modifier = Modifier.fillMaxWidth(),
                        faction = faction,
                        weaponImage = Uri.parse(it.weaponImage),
                        weaponName = it.weaponName ?: it.vehicleName,
                        medalType = it.medalType ?: MedalType.NONE,
                        totalKills = totalKills ?: 0,
                        totalVehiclesDestroyed = totalVehiclesDestroyed ?: 0,
                        totalHeadshotKills = totalHeadshotKills ?: 0,
                        NCKills = it.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.NC),
                        VSKills = it.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.VS),
                        TRKills = it.statMapping[WeaponEventType.KILLS]?.stats?.get(Faction.TR),
                    )
                }
            }
            LoadingOverlay(enabled = isLoading)
            ErrorOverlay(isError = isError)
        }
    }
}

@Preview
@Composable
fun Preview() {
    WeaponListCompose(
        weaponList = emptyList(),
        faction = Faction.TR,
        isLoading = true,
        isError = false,
    )
}
