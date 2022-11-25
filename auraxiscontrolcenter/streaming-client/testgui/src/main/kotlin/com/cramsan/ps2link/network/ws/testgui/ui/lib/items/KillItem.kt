package com.cramsan.ps2link.network.ws.testgui.ui.lib.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.network.ws.testgui.ui.lib.SlimButton
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Padding
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Size
import com.cramsan.ps2link.network.ws.testgui.ui.lib.toColor
import com.cramsan.ps2link.network.ws.testgui.ui.lib.widgets.FactionIcon
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KillItem(
    modifier: Modifier = Modifier,
    killType: KillType = KillType.UNKNOWN,
    faction: Faction = Faction.UNKNOWN,
    attacker: String? = null,
    time: Instant? = null,
    weaponName: String? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
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
                    text = when (killType) {
                        KillType.KILL -> "Kill"
                        KillType.KILLEDBY -> "Killed by"
                        KillType.SUICIDE -> "Suicide"
                        KillType.UNKNOWN -> "Unknown"
                    },
                    color = killType.toColor(),
                )
                FactionIcon(
                    modifier = Modifier.size(Size.xxlarge),
                    faction = faction,
                )
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = Padding.small)) {
                Text(attacker ?: "Unknown")
                Spacer(modifier = Modifier.size(Padding.medium))
                Text(
                    text = time?.let {
                        formatter.format(Date(it.toEpochMilliseconds()))
                    } ?: "Unknown",
                    style = MaterialTheme.typography.caption,
                )
            }
            Column(
                modifier = Modifier.size(Size.xxxlarge),
            ) {
                Text(weaponName ?: "Unknown")
            }
        }
    }
}

var formatter = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())
