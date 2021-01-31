package com.cramsan.ps2link.ui.items

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.FactionIcon

@Composable
fun KillItem(
    modifier: Modifier = Modifier,
    killType: KillType = KillType.UNKNOWN,
    faction: Faction = Faction.UNKNOWN,
    attacker: String? = null,
    time: Long? = null,
    weaponName: String? = null,
    weaponImage: Uri = Uri.EMPTY,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Column {
            FactionIcon(modifier = Modifier.size(Size.large), faction = faction)
            Text(
                stringResource(
                    when (killType) {
                        KillType.KILL -> R.string.text_killed_caps
                        KillType.KILLEDBY -> R.string.text_killed_by_caps
                        KillType.SUICIDE -> R.string.text_suicide_caps
                        KillType.UNKNOWN -> R.string.title_unkown
                    }
                )
            )
            Text(attacker ?: stringResource(R.string.text_unknown))
            Text(time.toString() ?: stringResource(R.string.text_unknown))
            Text(weaponName ?: stringResource(R.string.text_unknown))
            Text(weaponName ?: stringResource(R.string.text_unknown))
            /*
            CoilImage(
                data = weaponImage
            )
             */
        }
    }
}

@Preview
@Composable
fun ProfileKillPreview() {
    PS2Theme {
        KillItem(
            killType = KillType.KILL,
            faction = Faction.TR,
            attacker = "Cramsan",
            time = 100,
            weaponName = "Masamune",
        )
    }
}
