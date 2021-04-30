package com.cramsan.ps2link.ui.items

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.toImageRes

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
            Row {
                /*
                CoilImage(
                    data = weaponImage
                )
                 */
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row {
                        Text(stringResource(R.string.text_kills, totalKills))
                        NCKills?.let {
                            Text(stringResource(R.string.text_nc_, it))
                        }
                        TRKills?.let {
                            Text(stringResource(R.string.text_tr_, it))
                        }
                        VSKills?.let {
                            Text(stringResource(R.string.text_vs_, it))
                        }
                    }
                    Row {
                        Text(stringResource(R.string.text_headshots_, totalHeadshotKills))
                        Text(stringResource(R.string.text_vehicle_kills_, totalVehiclesDestroyed))
                    }
                }
                Image(
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
            totalKills = 1234,
            totalHeadshotKills = 21,
            VSKills = 32,
            TRKills = 54,
            NCKills = 12,
        )
    }
}
