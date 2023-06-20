package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.painterFromMedalType
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.NetworkImage

@Suppress("LongMethod")
@Composable
fun WeaponItem(
    modifier: Modifier = Modifier,
    useVerticalMode: Boolean = false,
    faction: Faction = Faction.UNKNOWN,
    weaponImage: String?,
    weaponName: String,
    medalType: MedalType = MedalType.NONE,
    totalKills: String,
    totalVehiclesDestroyed: String,
    totalHeadshotKills: String,
    vsKills: String? = null,
    trKills: String? = null,
    ncKills: String? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        if (useVerticalMode) {
            WeaponItemVertical(
                faction = faction,
                weaponImage = weaponImage,
                weaponName = weaponName,
                medalType = medalType,
                totalKills = totalKills,
                totalVehiclesDestroyed = totalVehiclesDestroyed,
                totalHeadshotKills = totalHeadshotKills,
                ncKills = ncKills,
                vsKills = vsKills,
                trKills = trKills,
            )
        } else {
            WeaponItemHorizontal(
                faction = faction,
                weaponImage = weaponImage,
                weaponName = weaponName,
                medalType = medalType,
                totalKills = totalKills,
                totalVehiclesDestroyed = totalVehiclesDestroyed,
                totalHeadshotKills = totalHeadshotKills,
                ncKills = ncKills,
                vsKills = vsKills,
                trKills = trKills,
            )
        }
    }
}

@Composable
internal fun WeaponItemVertical(
    faction: Faction,
    weaponImage: String?,
    weaponName: String,
    medalType: MedalType,
    totalKills: String,
    totalVehiclesDestroyed: String,
    totalHeadshotKills: String,
    vsKills: String? = null,
    trKills: String? = null,
    ncKills: String? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(weaponName)
        Row(
            verticalAlignment = CenterVertically,
        ) {
            NetworkImage(
                modifier = Modifier
                    .height(Size.xxxlarge)
                    .weight(1f),
                imageUrl = weaponImage,
            )

            val painter = painterFromMedalType(medalType)

            Image(
                modifier = Modifier
                    .size(Size.xxlarge),
                painter = painter,
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.small),
        ) {
            Text(totalKills)
            Spacer(modifier = Modifier.height(Padding.xsmall))
            Row {
                if (faction != Faction.NC) {
                    ncKills?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.overline,
                        )
                    }
                }
                if (faction != Faction.TR) {
                    Spacer(modifier = Modifier.width(Padding.small))
                    trKills?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.overline,
                        )
                    }
                }
                if (faction != Faction.VS) {
                    Spacer(modifier = Modifier.width(Padding.small))
                    vsKills?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.overline,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Padding.xsmall))
            Text(
                totalHeadshotKills,
                style = MaterialTheme.typography.overline,
            )
            Spacer(modifier = Modifier.width(Padding.xsmall))
            Text(
                totalVehiclesDestroyed,
                style = MaterialTheme.typography.overline,
            )
        }
    }
}

@Composable
internal fun WeaponItemHorizontal(
    faction: Faction = Faction.UNKNOWN,
    weaponImage: String?,
    weaponName: String,
    medalType: MedalType = MedalType.NONE,
    totalKills: String,
    totalVehiclesDestroyed: String,
    totalHeadshotKills: String,
    vsKills: String? = null,
    trKills: String? = null,
    ncKills: String? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(weaponName)
        Row(
            verticalAlignment = CenterVertically,
        ) {
            NetworkImage(
                modifier = Modifier
                    .size(Size.xxxlarge, Size.xxlarge),
                imageUrl = weaponImage,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(Padding.small),
            ) {
                Text(totalKills)
                Spacer(modifier = Modifier.height(Padding.xsmall))
                Row {
                    if (faction != Faction.NC) {
                        ncKills?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.overline,
                            )
                        }
                    }
                    if (faction != Faction.TR) {
                        Spacer(modifier = Modifier.width(Padding.small))
                        trKills?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.overline,
                            )
                        }
                    }
                    if (faction != Faction.VS) {
                        Spacer(modifier = Modifier.width(Padding.small))
                        vsKills?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.overline,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Padding.xsmall))
                Text(
                    totalHeadshotKills,
                    style = MaterialTheme.typography.overline,
                )
                Spacer(modifier = Modifier.width(Padding.xsmall))
                Text(
                    totalVehiclesDestroyed,
                    style = MaterialTheme.typography.overline,
                )
            }
            Image(
                modifier = Modifier
                    .size(Size.xxlarge)
                    .align(CenterVertically),
                painter = painterFromMedalType(medalType),
                contentDescription = null,
            )
        }
    }
}
