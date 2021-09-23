package com.cramsan.ps2link.ui.items

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toImageRes
import com.cramsan.ps2link.ui.widgets.NetworkImage

@Composable
fun WeaponItem(
    modifier: Modifier = Modifier,
    faction: Faction = Faction.UNKNOWN,
    weaponImage: Uri = Uri.EMPTY,
    weaponName: String? = null,
    medalType: MedalType = MedalType.NONE,
    totalKills: Long = 0,
    totalVehiclesDestroyed: Long = 0,
    totalHeadshotKills: Long = 0,
    VSKills: Long? = null,
    TRKills: Long? = null,
    NCKills: Long? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(weaponName ?: stringResource(R.string.text_unknown))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NetworkImage(
                    modifier = Modifier
                        .size(Size.xxxlarge, Size.xxlarge),
                    imageUrl = weaponImage.toString(),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Padding.small)
                ) {
                    Text(stringResource(R.string.text_kills, totalKills))
                    Spacer(modifier = Modifier.height(Padding.xsmall))
                    Row {
                        if (faction != Faction.NC) {
                            NCKills?.let {
                                Text(
                                    stringResource(R.string.text_nc_, it),
                                    style = MaterialTheme.typography.overline
                                )
                            }
                        }
                        if (faction != Faction.TR) {
                            Spacer(modifier = Modifier.width(Padding.small))
                            TRKills?.let {
                                Text(
                                    stringResource(R.string.text_tr_, it),
                                    style = MaterialTheme.typography.overline
                                )
                            }
                        }
                        if (faction != Faction.VS) {
                            Spacer(modifier = Modifier.width(Padding.small))
                            VSKills?.let {
                                Text(
                                    stringResource(R.string.text_vs_, it),
                                    style = MaterialTheme.typography.overline
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(Padding.xsmall))
                    Row {
                        Text(
                            stringResource(R.string.text_headshots_, totalHeadshotKills),
                            style = MaterialTheme.typography.overline
                        )
                        Spacer(modifier = Modifier.width(Padding.small))
                        Text(
                            stringResource(R.string.text_vehicle_kills_, totalVehiclesDestroyed),
                            style = MaterialTheme.typography.overline
                        )
                    }
                }
                Image(
                    modifier = Modifier
                        .size(Size.xxlarge)
                        .align(CenterVertically),
                    painter = painterResource(medalType.toImageRes()),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun WeaponItemPreview() {
    PS2Theme {
        WeaponItem(
            weaponName = "Pulsar C",
            totalKills = 123244,
            totalHeadshotKills = 23441,
            VSKills = 3242,
            TRKills = 5423,
            NCKills = 1223,
        )
    }
}
