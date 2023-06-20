package com.cramsan.ps2link.ui.items

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType

@Preview
@Composable
fun PreviewWeaponItem() {
    WeaponItemHorizontal(
        faction = Faction.VS,
        weaponImage = "https://census.daybreakgames.com/files/ps2/images/static/6015.png",
        weaponName = "Pulsar C",
        medalType = MedalType.AURAXIUM,
        totalKills = "1000",
        totalVehiclesDestroyed = "123",
        totalHeadshotKills = "52",
        ncKills = "52",
        vsKills = "62",
        trKills = "52",
    )
}
