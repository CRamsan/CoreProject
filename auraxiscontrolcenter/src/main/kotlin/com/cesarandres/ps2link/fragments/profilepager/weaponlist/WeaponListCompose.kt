package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import com.cramsan.ps2link.core.models.WeaponStatItem
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.WeaponItem

@Composable
fun WeaponListCompose(
    faction: Faction,
    weaponList: List<WeaponItem>,
    isLoading: Boolean,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(weaponList) {
                    val totalKills = filterStats(faction, WeaponEventType.KILLS, it.statMapping)
                    val totalVehiclesDestroyed = filterStats(faction, WeaponEventType.VEHICLE_KILLS, it.statMapping)
                    val totalHeadshotKills = filterStats(faction, WeaponEventType.HEADSHOT_KILLS, it.statMapping)
                    WeaponItem(
                        faction = faction,
                        weaponImage = Uri.parse(it.weaponImage),
                        weaponName = it.weaponName,
                        totalKills = totalKills,
                        totalVehiclesDestroyed = totalVehiclesDestroyed,
                        totalHeadshotKills = totalHeadshotKills,
                    )
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

fun filterStats(faction: Faction, weaponEventType: WeaponEventType, statMapping: Map<WeaponEventType, WeaponStatItem>): Long {
    return statMapping[weaponEventType]?.stats?.toList()?.sumOf {
        if (it.first == faction) {
            0
        } else {
            it.second
        }
    } ?: 0
}

@Preview
@Composable
fun Preview() {
    WeaponListCompose(
        weaponList = emptyList(),
        faction = Faction.TR,
        isLoading = true,
    )
}
