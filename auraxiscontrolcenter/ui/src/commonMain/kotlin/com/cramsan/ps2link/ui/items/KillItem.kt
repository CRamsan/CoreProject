package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toText
import com.cramsan.ps2link.ui.widgets.FactionIcon
import com.cramsan.ps2link.ui.widgets.NetworkImage

@Composable
fun KillItem(
    modifier: Modifier = Modifier,
    useVerticalMode: Boolean = false,
    killType: KillType = KillType.UNKNOWN,
    faction: Faction = Faction.UNKNOWN,
    attacker: String,
    time: String,
    weaponName: String,
    weaponImage: String?,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        if (useVerticalMode) {
            KillItemVertical(
                killType = killType,
                faction = faction,
                attacker = attacker,
                time = time,
                weaponName = weaponName,
                weaponImage = weaponImage,
            )
        } else {
            KillItemHorizontal(
                killType = killType,
                faction = faction,
                attacker = attacker,
                time = time,
                weaponName = weaponName,
                weaponImage = weaponImage,
            )
        }
    }
}

@Composable
internal fun KillItemHorizontal(
    killType: KillType,
    faction: Faction,
    attacker: String,
    time: String,
    weaponName: String,
    weaponImage: String?,
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
                text = killType.toText(),
                color = killType.toColor(),
            )
            FactionIcon(
                modifier = Modifier.size(Size.xxlarge),
                faction = faction,
            )
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = Padding.small)) {
            Text(attacker)
            Spacer(modifier = Modifier.size(Padding.medium))
            Text(
                text = time,
                style = MaterialTheme.typography.caption,
            )
        }
        Column(
            modifier = Modifier.size(Size.xxxlarge),
        ) {
            Text(weaponName)
            NetworkImage(
                modifier = Modifier.fillMaxSize(),
                imageUrl = weaponImage,
            )
        }
    }
}

@Composable
internal fun KillItemVertical(
    killType: KillType,
    faction: Faction,
    attacker: String,
    time: String,
    weaponName: String,
    weaponImage: String?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = killType.toText(),
                color = killType.toColor(),
            )
            Text(attacker)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Padding.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FactionIcon(
                modifier = Modifier.size(Size.xxlarge),
                faction = faction,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(weaponName)
                NetworkImage(
                    modifier = Modifier
                        .padding(horizontal = Padding.large)
                        .fillMaxWidth()
                        .height(Size.xxxlarge),
                    imageUrl = weaponImage,
                )
            }
        }
        Text(
            text = time,
            style = MaterialTheme.typography.caption,
        )
    }
}
