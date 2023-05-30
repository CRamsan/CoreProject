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
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toPainter
import com.cramsan.ps2link.ui.widgets.NetworkImage

@Composable
fun WeaponItem(
    modifier: Modifier = Modifier,
    faction: Faction = Faction.UNKNOWN,
    weaponImage: String?,
    weaponName: String,
    medalType: MedalType = MedalType.NONE,
    totalKills: String,
    totalVehiclesDestroyed: String,
    totalHeadshotKills: String,
    VSKills: String? = null,
    TRKills: String? = null,
    NCKills: String? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
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
                            NCKills?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.overline,
                                )
                            }
                        }
                        if (faction != Faction.TR) {
                            Spacer(modifier = Modifier.width(Padding.small))
                            TRKills?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.overline,
                                )
                            }
                        }
                        if (faction != Faction.VS) {
                            Spacer(modifier = Modifier.width(Padding.small))
                            VSKills?.let {
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
                    painter = medalType.toPainter(),
                    contentDescription = null,
                )
            }
        }
    }
}
