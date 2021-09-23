package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.widgets.FactionIcon
import com.cramsan.ps2link.ui.widgets.NetworkImage
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoilApi::class)
@Composable
fun KillItem(
    modifier: Modifier = Modifier,
    killType: KillType = KillType.UNKNOWN,
    faction: Faction = Faction.UNKNOWN,
    attacker: String? = null,
    time: Instant? = null,
    weaponName: String? = null,
    weaponImage: String? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.size(Size.xxxlarge),
                verticalArrangement = Arrangement.Center,
            ) {
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
                FactionIcon(
                    modifier = Modifier.size(Size.xxlarge),
                    faction = faction,
                )
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = Padding.small)) {
                Text(attacker ?: stringResource(R.string.text_unknown))
                Spacer(modifier = Modifier.size(Padding.medium))
                Text(
                    text = time?.let {
                        formatter.format(Date(it.toEpochMilliseconds()))
                    } ?: stringResource(R.string.text_unknown),
                    style = MaterialTheme.typography.caption,
                )
            }
            Column(
                modifier = Modifier.size(Size.xxxlarge),
            ) {
                Text(weaponName ?: stringResource(R.string.text_unknown))
                NetworkImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = weaponImage,
                )
            }
        }
    }
}

var formatter = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())

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
