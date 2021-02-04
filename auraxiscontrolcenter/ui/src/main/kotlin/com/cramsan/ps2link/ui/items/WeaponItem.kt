package com.cramsan.ps2link.ui.items

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun WeaponItem(
    modifier: Modifier = Modifier,
    faction: Faction = Faction.UNKNOWN,
    weaponImage: Uri = Uri.EMPTY,
    weaponName: String? = null,
    totalKills: Long? = null,
    totalVehiclesDestroyed: Long? = null,
    totalHeadshotKills: Long? = null,
    percentVSKills: Double? = null,
    percentTRKills: Double? = null,
    percentNCKills: Double? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row {
            Column {
                Text(weaponName ?: stringResource(R.string.text_unknown))
                /*
                CoilImage(
                    data = weaponImage
                )
                 */
            }
            Column {
                Row {
                    Text(totalKills?.toString() ?: stringResource(R.string.text_unknown))
                    Text(percentNCKills?.toString() ?: stringResource(R.string.text_unknown))
                    Text(percentTRKills?.toString() ?: stringResource(R.string.text_unknown))
                    Text(percentVSKills?.toString() ?: stringResource(R.string.text_unknown))
                }
                Row {
                    Text(totalHeadshotKills?.toString() ?: stringResource(R.string.text_unknown))
                    Text(totalVehiclesDestroyed?.toString() ?: stringResource(R.string.text_unknown))
                }
            }
            Column {
                /*
                CoilImage(
                    data = weaponImage
                )
                 */
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
            percentVSKills = 0.32,
            percentTRKills = 0.542,
            percentNCKills = 0.12,
        )
    }
}
