package com.cramsan.ps2link.ui.items

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.widgets.FactionIcon
import kotlinx.datetime.Instant

@Composable
fun KillItem(
    modifier: Modifier = Modifier,
    killType: KillType = KillType.UNKNOWN,
    faction: Faction = Faction.UNKNOWN,
    attacker: String? = null,
    time: Instant? = null,
    weaponName: String? = null,
    weaponImage: Uri = Uri.EMPTY,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(
                        when (killType) {
                            KillType.KILL -> R.string.text_killed_caps
                            KillType.KILLEDBY -> R.string.text_killed_by_caps
                            KillType.SUICIDE -> R.string.text_suicide_caps
                            KillType.UNKNOWN -> R.string.title_unkown
                        }
                    ),
                    color = killType.toColor(),
                )
                FactionIcon(modifier = Modifier.size(Size.large), faction = faction)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(attacker ?: stringResource(R.string.text_unknown))
                Text(time.toString() ?: stringResource(R.string.text_unknown))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(weaponName ?: stringResource(R.string.text_unknown))
                /*
                Image(
                    painter = rememberCoilPainter(
                        request = weaponImage,
                    ),
                    contentDescription = ""
                )
                 */
            }
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
            time = Instant.DISTANT_FUTURE,
            weaponName = "Masamune",
        )
    }
}
